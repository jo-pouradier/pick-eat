from typing import Optional
from uuid import UUID, uuid4
from sqlmodel import Field, Relationship, SQLModel


class BillPartModel(SQLModel, table=True):
    id: UUID = Field(default_factory=uuid4, primary_key=True)
    billId: UUID = Field(foreign_key="billmodel.id")
    bill: "BillModel" = Relationship(back_populates="parts")
    userId: Optional[UUID] = None
    price: Optional[float] = None
    text: Optional[str] = None

    model_config = {"from_attributes": True}
