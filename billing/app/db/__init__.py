import logging
import os
from typing import Annotated
from fastapi import Depends
from sqlmodel import SQLModel, Session
from sqlalchemy import create_engine

# mandatory
from .models.BillModel import BillModel
from .models.BillPartModel import BillPartModel

# set up logging
uvicorn_logger = logging.getLogger("uvicorn")
sqlmodel_logger = logging.getLogger("sqlalchemy.engine")
# sqlmodel_engine_logger = logging.getLogger("sqlalchemy.engine.Engine")

sqlmodel_logger.handlers = uvicorn_logger.handlers
sqlmodel_logger.setLevel(uvicorn_logger.level)
# sqlmodel_engine_logger.handlers = uvicorn_logger.handlers
# sqlmodel_engine_logger.setLevel(uvicorn_logger.level)

URL = os.getenv("DB_URL")
if URL is None:
    sqlmodel_logger.log(
        msg="DB_URL is not set, using an sqlite database", level=logging.WARNING
    )
    URL = "sqlite:///billing.db"
ENGINE = create_engine(URL, connect_args={"check_same_thread": False})

SQLModel.metadata.create_all(ENGINE)


def get_session():
    if ENGINE is None:
        raise Exception("DB is not initialized")
    with Session(ENGINE) as session:
        yield session


SessionDep = Annotated[Session, Depends(get_session)]
