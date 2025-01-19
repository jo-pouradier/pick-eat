from tkinter import W
from typing import List, Optional
from uuid import UUID
from pydantic import BaseModel

from .BillPartDTO import BillPartDTO


class BillDTO(BaseModel):
    id: Optional[UUID] = None
    userId: UUID
    eventId: UUID
    bucketName: Optional[str] = None
    path: Optional[str] = None
    parts: Optional[List[BillPartDTO]] = None
    total_price: Optional[float] = None

    model_config = {"from_attributes": True}
