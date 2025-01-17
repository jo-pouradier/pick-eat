from typing import Optional
from uuid import UUID, uuid4
from sqlmodel import Field, Relationship, SQLModel

from .BillPartModel import BillPartModel


class BillModel(SQLModel, table=True):
    id: UUID = Field(default_factory=uuid4, primary_key=True)
    userId: UUID = Field(index=True)
    eventId: UUID = Field(index=True)
    path: str
    bucketName: Optional[str] = None
    total_price: Optional[float] = None
    parts: Optional[list[BillPartModel]] = Relationship(back_populates="bill")

    model_config = {"from_attributes": True}
