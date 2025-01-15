import requests
import json
import os
import time
import re

# Clé API Google
API_KEY = ""
PHOTO_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo"
INPUT_FILE = os.path.join(".", "get_restaurants_data", "data", "all_restaurants_lyon.json")
OUTPUT_DIR = os.path.join(".", "get_restaurants_data", "data", "restaurants_photos")

def fetch_and_save_photo(api_key, photo_reference, output_path, max_width=400):
    params = {
        "photoreference": photo_reference,
        "key": api_key,
        "maxwidth": max_width
    }
    response = requests.get(PHOTO_BASE_URL, params=params, stream=True)
    
    if response.status_code == 200:
        with open(output_path, "wb") as file:
            for chunk in response.iter_content(1024):
                file.write(chunk)
        print(f"Photo sauvegardée dans {output_path}")
    else:
        print(f"Erreur lors du téléchargement de la photo : {response.status_code}")

def get_photos_for_all_restaurants(api_key, input_file, output_dir, limit=None):
    # Charger les restaurants depuis le fichier JSON
    with open(input_file, "r", encoding="utf-8") as file:
        restaurants = json.load(file)

    # Créer le dossier de sortie s'il n'existe pas
    os.makedirs(output_dir, exist_ok=True)
    
    request_count = 0
    processed_count = 0

    for restaurant in restaurants:
        # Arrêter le traitement si la limite est atteinte
        if limit is not None and processed_count >= limit:
            break

        # Vérifier si une photo existe pour le restaurant
        photos = restaurant.get("photos", [])
        if not photos:
            print(f"Aucune photo trouvée pour {restaurant.get('name', 'Restaurant inconnu')}")
            continue

        photo_reference = photos[0].get("photo_reference")
        if not photo_reference:
            continue

        # Définir le chemin de sortie pour la photo
        restaurant_name = restaurant.get("name", "unknown").replace(" ", "_").replace("/", "_").replace("|", "_").replace("&", "and")
        restaurant_name = re.sub(r'[<>:"/\\|?*]', '_', restaurant_name)
        output_path = os.path.join(output_dir, f"{restaurant_name}.jpg")

        # Vérifier si le fichier existe déjà
        if os.path.exists(output_path):
            print(f"Photo déjà existante pour {restaurant.get('name', 'Restaurant inconnu')}. Requête ignorée.")
            processed_count += 1
            continue

        # Télécharger et sauvegarder la photo
        print("processed_count :", processed_count)
        print(f"Téléchargement de la photo pour {restaurant.get('name', 'Restaurant inconnu')}...")
        fetch_and_save_photo(api_key, photo_reference, output_path)

        # Respecter les limites de quota en ajoutant une pause
        request_count += 1
        processed_count += 1
        if request_count % 10 == 0:
            print("Pause de 1 seconde pour éviter les limites de quota...")
            time.sleep(0.5)

    print(f"Téléchargement des photos terminé pour {processed_count} restaurants. Photos sauvegardées dans {output_dir}")

def main():
    # n = 2  # Nombre de restaurants à traiter pour les tests
    get_photos_for_all_restaurants(API_KEY, INPUT_FILE, OUTPUT_DIR)

if __name__ == "__main__":
    main()
