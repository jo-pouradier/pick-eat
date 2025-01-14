from typing import Optional
from uuid import UUID, uuid4
from sqlmodel import Field, SQLModel


class BillPartModel(SQLModel, table=True):
    id: UUID = Field(default_factory=uuid4, primary_key=True)
    billId: UUID = Field(foreign_key="billmodel.id")
    userId: UUID = Field()
    price: Optional[float] = Field(default=None)
    text: Optional[str] = Field(default=None)