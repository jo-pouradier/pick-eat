import os
from pathlib import Path
import shutil
from uuid import UUID
from fastapi import APIRouter, File, HTTPException, UploadFile
from fastapi.responses import FileResponse

from ..service import process_bill_img
from ..dto.mapper import MapperDTO
from ..dto import BillDTO
from ..db import BillModel, SessionDep

router = APIRouter()

# Directory to store images locally (for simplicity)
ROOT_PATH = os.getenv("SSD_STORAGE_PATH", "..")
IMAGE_DIR = Path("/bills")
IMAGE_DIR.mkdir(exist_ok=True)


@router.get("/{bill_id}", description="Get a bill by ID")
async def get_bill(bill_id: UUID, session: SessionDep) -> BillDTO | None:
    bill = session.get(BillModel, bill_id)
    if bill is None:
        return None
    return BillDTO.model_validate(bill)


@router.post("/", description="Create a new bill")
async def create_bill(bill: BillDTO, session: SessionDep) -> BillDTO | None:
    bill.id = None  # force a new UUID by orm
    try:
        newBill = MapperDTO.to_bill(bill)
        print("newBill", newBill)
        session.add(newBill)
        session.commit()
        session.refresh(newBill)

    except Exception as e:
        print(e)
        session.rollback()
        return None

    return BillDTO.model_validate(newBill)


@router.put("/{bill_id}", description="Update a bill by ID")
async def update_bill(
    bill_id: UUID, newBill: BillDTO, session: SessionDep
) -> BillDTO | None:
    try:
        bill = session.get(BillModel, bill_id)
        bill.bucketName = newBill.bucketName
        bill.path = newBill.path
        bill.userId = newBill.userId
        session.commit()
        session.refresh(bill)
        return BillDTO.model_validate(bill)
    except Exception as e:
        print(e)
        session.rollback()
        return None


@router.get("/image/{bill_id}", description="Get the image of a bill by ID")
async def get_bill_image(bill_id: UUID, session: SessionDep):
    bill = session.get(BillModel, bill_id)
    if bill is None:
        raise HTTPException(status_code=404, detail="Bill not found")

    # Return the image file
    return FileResponse(bill.path)


@router.post("/image/{bill_id}", description="Upload an image for a bill by ID")
async def upload_bill_image(
    bill_id: UUID, session: SessionDep, file: UploadFile = File(...)
):
    bill = session.get(BillModel, bill_id)
    if file.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(
            status_code=400, detail="Invalid file type. Only JPEG or PNG is allowed."
        )

    file_path = IMAGE_DIR / f"{bill_id}_{file.filename}"
    with open(file_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)
    bill.path = str(file_path)
    session.commit()
    process_bill_img.process_bill_img(bill, file.file)
    return {"filename": file.filename, "path": str(file_path)}
