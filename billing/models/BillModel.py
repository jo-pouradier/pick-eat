from typing import Optional
from uuid import UUID, uuid4
from sqlmodel import Field, Relationship, SQLModel

from .BillPartModel import BillPartModel


class BillModel(SQLModel, table=True):
    id: UUID = Field(default_factory=uuid4, primary_key=True)
    userId: UUID = Field()
    bucketName: Optional[str] = Field(default=None)
    path: str = Field(default=None)
    # list of bill parts
    parts : list[BillPartModel] = Relationship(back_populates="billmodel")
