import json
import os
from queue import Queue
from uuid import UUID
import dotenv
import stomp
import logging

from .process import process_bill_parts

from ..utils.log import get_logger

dotenv.load_dotenv()


class CustomBrokerAdapter(logging.LoggerAdapter):
    def process(self, msg, kwargs):
        return f"[BrokerManager] {msg}", kwargs


class CustomStompAdapter(logging.LoggerAdapter):
    def process(self, msg, kwargs):
        return f"[Stomp] {msg}", kwargs


class BrokerManager(stomp.ConnectionListener):
    PROCESS = {"ANALYSE-OUT": process_bill_parts}

    CONN: stomp.Connection = None

    LOGGER = CustomBrokerAdapter(logging.getLogger("uvicorn"), {})

    BROKER_USER = os.getenv("BROKER_USER")
    if BROKER_USER is None:
        LOGGER.warning("BROKER_USER not set, using default")
        BROKER_USER = "admin"
    BROKER_PASS = os.getenv("BROKER_PASS")
    if BROKER_PASS is None:
        LOGGER.warning("BROKER_PASS not set, using default")
        BROKER_PASS = "admin"
    BROKER_HOST = os.getenv("BROKER_HOST")
    if BROKER_HOST is None:
        LOGGER.warning("BROKER_HOST not set, using default")
        BROKER_HOST = "localhost"
    BROKER_PORT = 61613
    if BROKER_PORT is None:
        LOGGER.warning("BROKER_PORT not set, using default")
        BROKER_PORT = 61613
    BILL_PROCESS_QUEUE = "ANALYSE-IN"
    MESSAGES = Queue()
    # Configure `stomp` logger to use the same handlers and level as `uvicorn`
    stomp_logger = logging.getLogger("stomp.py")
    stomp_logger.handlers = logging.getLogger("uvicorn").handlers
    stomp_logger.setLevel(logging.getLogger("uvicorn").level)

    @classmethod
    def init(cls):
        if cls.CONN is None:
            try:
                cls.CONN = stomp.Connection([(cls.BROKER_HOST, cls.BROKER_PORT)])
                cls.CONN.set_listener("", cls())
                cls.connect_and_subscribe()
            except Exception as e:
                cls.LOGGER.error(f"Error while connecting to broker: {e}")

    @classmethod
    def get_conn(cls):
        if cls.CONN is None:
            cls.init()
        return cls.CONN

    @classmethod
    def connect_and_subscribe(cls):
        cls.init()
        cls.LOGGER.info("Subscribing to queue: ANALYSE-OUT")
        cls.CONN.connect(cls.BROKER_USER, cls.BROKER_PASS, wait=True)
        cls.CONN.subscribe(destination="ANALYSE-OUT", id=1, ack="auto")
        cls.LOGGER.info("Subscribed to queue: ANALYSE-OUT")

    def on_error(self, frame):
        self.LOGGER.error('received an error "%s"' % frame.body)

    def on_message(self, frame):
        destination = frame.headers.get("destination").replace("/queue/", "")
        self.LOGGER.info(f"received a message from {destination}: {frame.body}")
        if destination in self.PROCESS:
            self.PROCESS[destination](frame.body)
        self.LOGGER.info("processed message")

    def on_disconnected(self):
        self.LOGGER.warning("Broker got disconnected")
        self.connect_and_subscribe()

    def on_connected(self, frame):
        self.LOGGER.info("Broker connected")
        if not self.MESSAGES.empty():
            self.LOGGER.info("Sending pending messages")
        while not self.MESSAGES.empty():
            msg = self.MESSAGES.get()
            self.send(*msg)
        return super().on_connected(frame)

    @classmethod
    def send(cls, body, destination, content_type="text/plain"):
        try:
            cls.get_conn().send(
                body=body, destination=destination, content_type=content_type
            )
            cls.LOGGER.info(f"Sent message to {destination}")
        except Exception as e:
            cls.LOGGER.error(f"Error while sending message to broker: {e}")
            cls.MESSAGES.put((body, destination, content_type))
        return True

    @classmethod
    def send_bill_text_to_process(cls, text: str, bill_id: UUID):
        data = json.dumps({"text": text, "bill_id": str(bill_id)})
        cls.send(body=data, destination="ANALYSE-IN", content_type="application/json")
        return True
