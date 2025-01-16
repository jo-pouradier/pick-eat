from typing import Optional
from uuid import UUID, uuid4
from sqlmodel import Field, Relationship, SQLModel


class BillPartModel(SQLModel, table=True):
    id: UUID = Field(default_factory=uuid4, primary_key=True)
    billId: UUID = Field(foreign_key="billmodel.id")
    bill: "BillModel" = Relationship(back_populates="parts")
    userId: UUID
    price: Optional[float]
    text: Optional[str]

    model_config = {
        "from_attributes": True
    }

