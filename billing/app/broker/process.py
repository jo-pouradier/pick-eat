from dataclasses import dataclass
import json
import logging

from sqlmodel import Session

from ..db import ENGINE, BillPartModel, BillModel
from ..utils import get_logger
from typing import List, Dict
from uuid import UUID


class CustomProcessAdapter(logging.LoggerAdapter):
    def process(self, msg, kwargs):
        return super().process(f"[BrokerProcess] {msg}", kwargs)


logger = CustomProcessAdapter(get_logger(), {})


def process_bill_parts(body: str) -> None:
    data = json.loads(body)
    # is dict ?
    if not isinstance(data, dict):
        logger.error("Message is not a dict")
        return
    uuid = data.get("billId", None)
    if uuid is None:
        logger.error("No billId in message")
        return
    bill_id = UUID(uuid)
    with Session(ENGINE, autoflush=False) as session:
        try:
            for item in data["data"]:
                if item.get("total_cost", None) is not None:
                    # session.get(BillPartModel, bill_id).totalCost = item["total_cost"]
                    bill = session.get(BillModel, bill_id)
                    bill.total_price = item["total_cost"]
                    session.add(bill)
                    continue
                for _ in range(0, item.get("quantity", 0)):
                    bill_part = BillPartModel(
                        billId=bill_id, text=item["name"], price=item["unit_price"]
                    )
                    logger.debug(bill_part)
                    # bill_part.save()
                    session.add(bill_part)
            logger.info(f"Bill {bill_id} processed")

            session.commit()
        except Exception as e:
            logger.error(f"Error while processing bill {bill_id}: {e}")
            session.rollback()
