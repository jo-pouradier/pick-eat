import dotenv
from fastapi import FastAPI
from fastapi.concurrency import asynccontextmanager

from db import db
from middleware import jwt_mdlw
from router import billsRouter, partsRouter


@asynccontextmanager
async def app_lifespan(app: FastAPI):
    dotenv.load_dotenv()
    print("Starting database")
    db.init_db()
    print("Database started")
    yield
    print("Closing database")


app = FastAPI(lifespan=app_lifespan)
app.add_middleware(jwt_mdlw.JWTMiddleware)

# Include routers
app.include_router(billsRouter.router, prefix="/bills", tags=["Bills"])
app.include_router(partsRouter.router, prefix="/parts", tags=["Parts"])
