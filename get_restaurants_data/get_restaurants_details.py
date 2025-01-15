import requests
import json
import time
import os

API_KEY = ""
DETAILS_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json"
INPUT_FILE = os.path.join(".", "get_restaurants_data", "data", "all_restaurants_lyon.json")
OUTPUT_FILE = os.path.join(".", "get_restaurants_data", "data", "restaurant_details.json")

def fetch_place_details(api_key, place_id):
    """Récupère les détails d'un lieu à partir de son place_id via l'API Place Details."""
    params = {
        "place_id": place_id,
        "key": api_key,
        "fields": "name,rating,formatted_address,geometry,formatted_phone_number,opening_hours,website"
    }
    response = requests.get(DETAILS_BASE_URL, params=params)
    return response.json()

def get_details_for_all_restaurants(api_key, input_file, output_file):
    """Récupère les détails de tous les restaurants à partir d'un fichier JSON."""
    # Charger les restaurants depuis le fichier JSON
    with open(input_file, "r", encoding="utf-8") as file:
        restaurants = json.load(file)
    
    restaurant_details = {}
    request_count = 0

    for restaurant in restaurants:
        place_id = restaurant.get("place_id")
        if not place_id:
            continue

        print(f"Récupération des détails pour place_id: {place_id}...")

        # Faire la requête API pour récupérer les détails
        details = fetch_place_details(api_key, place_id)

        # Vérifier si la requête a réussi
        if details.get("status") == "OK":
            restaurant_details[place_id] = details["result"]
        else:
            print(f"Erreur pour place_id {place_id}: {details.get('status')}")

        # Respecter les limites de quota en ajoutant une pause
        request_count += 1
        if request_count % 10 == 0:
            print("Pause de 1 seconde pour éviter les limites de quota...")
            time.sleep(1)

    # Sauvegarder les résultats dans un fichier JSON
    with open(output_file, "w", encoding="utf-8") as file:
        json.dump(restaurant_details, file, ensure_ascii=False, indent=4)

    print(f"Détails de {len(restaurant_details)} restaurants sauvegardés dans {output_file}")

# Appel de la fonction pour récupérer les détails
get_details_for_all_restaurants(API_KEY, INPUT_FILE, OUTPUT_FILE)
