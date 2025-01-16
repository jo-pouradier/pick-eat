from tkinter import W
from typing import List, Optional
from uuid import UUID
from pydantic import BaseModel

from .BillPartDTO import BillPartDTO


class BillDTO(BaseModel):
    id: Optional[UUID] = None
    userId: UUID
    bucketName: Optional[str] = None
    path: str
    parts: Optional[List[BillPartDTO]] = None

    model_config = {
        "from_attributes": True
    }