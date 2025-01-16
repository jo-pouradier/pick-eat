from uuid import UUID
from fastapi import APIRouter, Depends
from sqlalchemy import select

from ..dto.mapper import MapperDTO
from ..dto import BillPartDTO
from ..db import BillPartModel, SessionDep

router = APIRouter()


@router.get("/{user_uuid}", description="Get all parts assigned to a user")
async def get_parts(user_uuid: UUID, session: SessionDep) -> list[BillPartDTO]:
    parts = session.exec(
        select(BillPartModel).where(BillPartModel.userId == user_uuid)
    ).all()
    print("parts: ", parts)
    if parts is None:
        return []
    return [BillPartDTO.model_validate(part[0]) for part in parts]


@router.put("/{part_id}", description="Update a part by ID")
async def update_part(part_id: UUID, newPart: BillPartDTO, session: SessionDep):
    try:
        part = session.get(BillPartModel, part_id)
        part.price = newPart.price
        part.text = newPart.text
        part.userId = newPart.userId
        session.commit()
        session.refresh(part)
        return BillPartDTO.model_validate(part)
    except Exception as e:
        print(e)
        session.rollback()
        return None



@router.post("", description="Create a new part")
async def create_part(part: BillPartDTO, session: SessionDep):
    newPart = MapperDTO.to_bill_part(part)
    newPart.id = None  # force a new UUID by orm
    try:
        session.add(newPart)
        session.commit()
        session.refresh(newPart)
    except Exception as e:
        print(e)
        session.rollback()
        return None

    return BillPartDTO.model_validate(newPart)
