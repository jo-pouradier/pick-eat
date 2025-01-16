import os
from typing import Annotated
from fastapi import Depends
from sqlmodel import SQLModel, Session
from sqlalchemy import create_engine

# mandatory
from .models.BillModel import BillModel
from .models.BillPartModel import BillPartModel

URL = os.getenv("DB_URL")
if URL is None:
    print("DB_URL is not set, using an sqlite database")
    URL = "sqlite:///billing.db"
ENGINE = create_engine(URL, echo=True, connect_args={"check_same_thread": False})

SQLModel.metadata.create_all(ENGINE)


def get_session():
    if ENGINE is None:
        raise Exception("DB is not initialized")
    with Session(ENGINE) as session:
        yield session


SessionDep = Annotated[Session, Depends(get_session)]
