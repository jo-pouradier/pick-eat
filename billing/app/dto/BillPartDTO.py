from typing import Optional
from uuid import UUID
from pydantic import BaseModel


class BillPartDTO(BaseModel):
    id: Optional[UUID] = None
    billId: UUID
    userId: Optional[UUID] = None
    price: Optional[float] = None
    text: Optional[str] = None

    model_config = {"from_attributes": True}
