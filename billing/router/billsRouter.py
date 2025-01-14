from fastapi import APIRouter
from db.db import SessionDep
from models import BillModel

router = APIRouter()


@router.get("/{bill_id}", description="Get a bill by ID")
async def get_bill(bill_id: str, session: SessionDep):
    bill = session.get(BillModel, bill_id)
    return bill


@router.post("", description="Create a new bill")
async def create_bill(bill: BillModel, session: SessionDep):
    session.add(bill)
    session.commit()
    return bill


@router.put("/{bill_id}", description="Update a bill by ID")
async def update_bill(bill_id: str, newBill: BillModel, session: SessionDep):
    bill = session.get(BillModel, bill_id)
    bill.bucketName = newBill.bucketName
    bill.path = newBill.path
    bill.userId = newBill.userId
    session.commit()
    return bill
