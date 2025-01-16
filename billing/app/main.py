import dotenv
from fastapi import FastAPI
from fastapi.concurrency import asynccontextmanager

from .middleware import jwt_mdlw
from .router import billsRouter, partsRouter

dotenv.load_dotenv()


@asynccontextmanager
async def app_lifespan(app: FastAPI):
    print("Starting database")
    print("Database started")
    yield
    print("Closing database")


app = FastAPI(lifespan=app_lifespan)
app.add_middleware(jwt_mdlw.JWTMiddleware)

# Include routers
app.include_router(partsRouter.router, prefix="/parts", tags=["Parts"])
app.include_router(billsRouter.router, prefix="/bills", tags=["Bills"])
