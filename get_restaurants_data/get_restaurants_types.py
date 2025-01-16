import requests
import json
import time
import os

API_KEY = ""
DETAILS_BASE_URL = "https://places.googleapis.com/v1/places/"
INPUT_FILE = os.path.join(".", "get_restaurants_data", "data", "all_restaurants_lyon.json")
OUTPUT_FILE = os.path.join(".", "get_restaurants_data", "data", "restaurant_types.json")

def fetch_restaurant_types(api_key, place_id):
    place_url = f"{DETAILS_BASE_URL}{place_id}?key={api_key}&fields=types"
    response = requests.get(place_url)
    return response

def get_types_for_all_restaurants(api_key, input_file, output_file):
    # Charger les restaurants depuis le fichier JSON
    with open(input_file, "r", encoding="utf-8") as file:
        restaurants = json.load(file)
    
    restaurant_types = {}
    request_count = 0

    for restaurant in restaurants:
        place_id = restaurant.get("place_id")
        if not place_id:
            continue

        print(f"Récupération des types pour place_id: {place_id}...")

        # Faire la requête API pour récupérer les détails
        details = fetch_restaurant_types(api_key, place_id)

        # Vérifier si la requête a réussi
        if details.status_code == 200:
            restaurant_types[place_id] = details.json()
        else:
            print(f"Erreur pour place_id {place_id}.")

        # Respecter les limites de quota en ajoutant une pause
        request_count += 1
        print(f"Requête n°{request_count}/{len(restaurants)}")
        if request_count % 10 == 0:
            print("Pause de 0.5 seconde pour éviter les limites de quota...")
            time.sleep(0.5)

    # Sauvegarder les résultats dans un fichier JSON
    with open(output_file, "w", encoding="utf-8") as file:
        json.dump(restaurant_types, file, ensure_ascii=False, indent=4)

    print(f"Types de {len(restaurant_types)} restaurants sauvegardés dans {output_file}")

# Appel de la fonction pour récupérer les détails
get_types_for_all_restaurants(API_KEY, INPUT_FILE, OUTPUT_FILE)
