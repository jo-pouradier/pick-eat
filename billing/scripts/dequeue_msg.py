import os
import dotenv
import stomp

dotenv.load_dotenv()


class BrokerListener(stomp.ConnectionListener):
    def on_error(self, headers, message):
        print("received an error %s" % message)

    def on_message(self, headers, message):
        print("received a message %s" % message)


def dequeue_msg():
    host = os.getenv("BROKER_HOST", "localhost")
    print(host)
    port = os.getenv("BROKER_PORT", 61613)
    user = os.getenv("BROKER_USER", "admin")
    password = os.getenv("BROKER_PASS", "admin")
    conn = stomp.Connection([(host, port)])
    conn.connect(user, password)
    conn.subscribe("ANALYSE-OUT", BrokerListener(), ack="auto")


if __name__ == "__main__":
    dequeue_msg()
