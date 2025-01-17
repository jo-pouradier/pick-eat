from uuid import uuid4
from fastapi import Cookie
from fastapi.testclient import TestClient
import sys

sys.path.append(".")
from app.main import (
    app,
)  # Assurez-vous que l'application FastAPI est importée correctement

TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwic2NvcGUiOiJVU0VSIiwiaXNzIjoicGljay1lYXQiLCJleHAiOjE3NjgzODgzMDcsImlhdCI6MTczNjg1MjMwNywidXVpZCI6IjhiOGFjYTk4LWU5YjktNGFkNS1hOTAzLTc0MmU3NGVlMTViZiJ9.cp22oKCSuCwsOmCXntjx0ZKtIM6zPNF-yH-5v0q02rAK8_XcPCMHTV_u6PJkSs3djzK1TBPEjJODUPBdfBJt-h-L8XdEA-lJlgcB_wcVXSEfM-ZllIagdj9poUzGmQQSFIUy_u5OkqzP9zqOg0Sj7DEMhq8IntSHcK6rPpaQggb0N4VEgcBQ2hGvbyjYc6FsNE4p23V7FiML4P_eAFQmjT7eXY87ixLyXqBWT7-AOTqERQVN6BlPdFxC8QhJo1AXfWStcB9W2hk2Uz7sFkShk1BV0Ff0qCvYwUwXAv2Zv-B2ETLbrxqieXCkxPdi1k-G453PmMANZBpLFzuRsuOjFg"
cookie = Cookie(name="jwt", value=TOKEN)
client = TestClient(app, cookies={"jwt": TOKEN})


class TestBillsRouter:
    @classmethod
    def setup_class(cls):
        cls.user_uuid = uuid4()
        cls.eventId = uuid4()
        cls.bill = {
            "path": "test_path",
            "bucketName": "test_bucket",
            "userId": str(cls.user_uuid),
            "eventId": str(cls.eventId),
            "parts": [],
            "total_price": None
        }
        cls.client = TestClient(app, cookies={"jwt": TOKEN, "domain": "localhost:8080"})

    def test_post_get_bill(self):
        response = self.client.post("/bills/", json=self.bill)
        assert response.status_code == 200

        response_data = response.json()
        bill_id = response_data["id"]

        self.bill["id"] = bill_id
        assert response_data == self.bill

        response = self.client.get(f"/bills/{bill_id}")
        # Assert the response
        assert response.status_code == 200
        assert response.json() == self.bill
 
        response = self.client.get(f"/bills/event/{self.eventId}")
        # Assert the response
        assert response.status_code == 200
        assert response.json() == [self.bill]

    def test_put_bill(self):
        self.bill["bucketName"] = "new_bucket"
        id = self.bill.get("id", None)
        if id is None:
            response = self.client.post("/bills/", json=self.bill)
            assert response.status_code == 200
            response_data = response.json()
            id = response_data["id"]
            self.bill["id"] = id
        response = self.client.put(f"/bills/{self.bill['id']}", json=self.bill)
        # Assert the response
        assert response.status_code == 200
        assert response.json() == self.bill
