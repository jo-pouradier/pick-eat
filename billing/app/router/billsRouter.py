from uuid import UUID
from fastapi import APIRouter

from ..dto.mapper import MapperDTO
from ..dto import BillDTO
from ..db import BillModel, SessionDep

router = APIRouter()


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
