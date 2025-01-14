import jwt
from fastapi import HTTPException, Request, Response
from starlette.middleware.base import BaseHTTPMiddleware
# read file
JWT_SECRET_KEY = open("certs/public.pem").read() 
JWT_ALGORITHM = "HS256"

class JWTMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next):
        jwt_token = request.cookies.get("jwt")

        if request.url.path in ("/docs", "/redoc", "/openapi.json"):
            response = await call_next(request)
            return response
        
        if not jwt_token:
            return Response(status_code=401, content="JWT token not found in cookies")

        try:
            payload = jwt.decode(jwt_token, JWT_SECRET_KEY, algorithms=[JWT_ALGORITHM])
            
            if payload.get("scope") != "USER":
                return Response(status_code=403, content="Only connected users are allowed")
            
            user_uuid = payload.get("uuid")
            if not user_uuid:
                return Response(status_code=400, content="UUID missing in token")
            
            request.state.user_uuid = user_uuid
        
        except jwt.ExpiredSignatureError:
            print("JWT token has expired")
            return  Response(status_code=401, content="JWT token has expired")
        except jwt.InvalidTokenError:
            print("Invalid JWT token")
            return  Response(status_code=401, content="Invalid JWT token")
        except Exception as e:
            print(f"Error in JWT middleware: {e}")
            return  Response(status_code=500, content="Internal server error")
        
        response = await call_next(request)
        return response