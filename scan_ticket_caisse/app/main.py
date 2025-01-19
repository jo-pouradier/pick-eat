import time
import logging
from connection import BrokerManager
from uuid import UUID

# Configurer le logger principal
logging.basicConfig(level=logging.INFO)
LOGGER = logging.getLogger("analyze")

def main():
    try:
        # Initialiser le BrokerManager
        BrokerManager.init()

        # Exécuter une boucle infinie pour écouter les messages du broker
        LOGGER.info("Listening for messages on the broker...")

        while True:
            # Restez à l'écoute des messages entrants
            time.sleep(1)  # Le temps d'attente peut être ajusté en fonction des besoins

    except KeyboardInterrupt:
        LOGGER.info("Shutting down gracefully...")

if __name__ == "__main__":
    main()
