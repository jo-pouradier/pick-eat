
import json
from uuid import uuid4
from fastapi import Cookie
from fastapi.testclient import TestClient
import sys

sys.path.append(".")
from app.main import app  # Assurez-vous que l'application FastAPI est importée correctement

TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwic2NvcGUiOiJVU0VSIiwiaXNzIjoicGljay1lYXQiLCJleHAiOjE3NjgzODgzMDcsImlhdCI6MTczNjg1MjMwNywidXVpZCI6IjhiOGFjYTk4LWU5YjktNGFkNS1hOTAzLTc0MmU3NGVlMTViZiJ9.cp22oKCSuCwsOmCXntjx0ZKtIM6zPNF-yH-5v0q02rAK8_XcPCMHTV_u6PJkSs3djzK1TBPEjJODUPBdfBJt-h-L8XdEA-lJlgcB_wcVXSEfM-ZllIagdj9poUzGmQQSFIUy_u5OkqzP9zqOg0Sj7DEMhq8IntSHcK6rPpaQggb0N4VEgcBQ2hGvbyjYc6FsNE4p23V7FiML4P_eAFQmjT7eXY87ixLyXqBWT7-AOTqERQVN6BlPdFxC8QhJo1AXfWStcB9W2hk2Uz7sFkShk1BV0Ff0qCvYwUwXAv2Zv-B2ETLbrxqieXCkxPdi1k-G453PmMANZBpLFzuRsuOjFg"
cookie = Cookie(name="jwt", value=TOKEN)
client = TestClient(app, cookies={"jwt": TOKEN})

class TestPartsRouter:
    @classmethod
    def setup_class(cls):
        cls.user_uuid = uuid4()
        cls.bill = {
            "path": "test_path",
            "bucketName": "test_bucket",
            "userId": str(cls.user_uuid),
            "parts": []
        }
        cls.client = TestClient(app, cookies={"jwt": TOKEN})
        # create a bill
        response = cls.client.post("/bills/", json=cls.bill)
        assert response.status_code == 200
        response_data = response.json()
        cls.bill["id"] = response_data["id"]
        # create a part
        cls.part = {
            "price": 100.0,
            "text": "Test part",
            "userId": str(cls.user_uuid),
            "billId": str(cls.bill["id"])
        }

    def test_post_get_part(self):
        response = self.client.post("/parts", json=self.part)
        assert response.status_code == 200
        response_data = response.json()
        self.part["id"] = response_data["id"]
        self.part == response_data

        response = self.client.get(f"/parts/{self.user_uuid}")
        assert response.status_code == 200
        assert response.json() == [self.part]
        
    
    def test_get_parts(self):
        newPart = self.part.copy()
        newPart["text"] = "Test part 2"
        response = self.client.put(f"/parts/{self.part['id']}", json=newPart)
        assert response.status_code == 200
        assert response.json() == newPart