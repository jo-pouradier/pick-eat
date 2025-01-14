import os
from typing import Annotated

from fastapi import Depends
from sqlalchemy import Engine, create_engine
from sqlmodel import SQLModel, Session

engine = None

def init_db():
    url = os.getenv("DB_URL")
    print("DB initializing")

    if url is None:
        url = "sqlite:///:memory:"
        print("DB_URL is not set, using in-memory SQLite")

    from models import BillModel, BillPartModel # import to load for the engine
    engine = create_engine(url, echo=True)
    SQLModel.metadata.create_all(engine)

def get_session(): 
    if engine is None:
        raise Exception("DB is not initialized")
    with Session(engine) as session:
        yield session

SessionDep = Annotated[Session, Depends(get_session)]