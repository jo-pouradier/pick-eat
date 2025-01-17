from ..db.models import BillModel, BillPartModel
from .BillDTO import BillDTO


class MapperDTO:
    @staticmethod
    def to_bill(billDTO: BillDTO) -> BillModel:
        print("billDTO ", billDTO)
        bill = BillModel(
            id=billDTO.id,
            bucketName=billDTO.bucketName,
            path=billDTO.path,
            userId=billDTO.userId,
            eventId=billDTO.eventId,
        )
        if billDTO.parts is not None:
            bill.parts = [BillPartModel(**part.model_dump()) for part in billDTO.parts]
        return bill

    @staticmethod
    def to_bill_part(part: BillPartModel) -> BillPartModel:
        dump = part.model_dump()
        return BillPartModel(**dump)
