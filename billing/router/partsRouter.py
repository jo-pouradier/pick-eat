from fastapi import APIRouter, Depends
from db.db import SessionDep
from models import BillPartModel

router = APIRouter()


@router.get("/{user_uuid}", description="Get all parts assigned to a user")
async def get_parts(user_uuid: str, session: SessionDep):
    parts = session.query(BillPartModel).filter(BillPartModel.userId == user_uuid).all()
    return parts


@router.put("/{part_id}", description="Update a part by ID")
async def update_part(part_id: str, newPart: BillPartModel, session: SessionDep):
    part = session.get(BillPartModel, part_id)
    part.price = newPart.price
    part.text = newPart.text
    part.userId = newPart.userId
    session.commit()
    return part


@router.post("", description="Create a new part")
async def create_part(part: BillPartModel, session: SessionDep):
    session.add(part)
    session.commit()
    return part
