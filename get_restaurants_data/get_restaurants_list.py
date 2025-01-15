import requests
import json
import time
import os

API_KEY = ""
BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
OUTPUT_FILE = os.path.join(".", "get_restaurants_data", "data", "all_restaurants_lyon.json")

def fetch_restaurants_in_area(api_key, location, radius=1000):
    """Effectue une requête à l'API Google Places pour un point spécifique."""
    params = {
        "location": location,
        "radius": radius,
        "type": "restaurant",
        "key": api_key
    }
    restaurants = []
    request_count = 0

    while True:
        # Envoi de la requête à l'API
        response = requests.get(BASE_URL, params=params).json()
        request_count += 1
        print(f"Requête #{request_count}: {len(response.get('results', []))} résultats récupérés.")

        # Ajouter les résultats à la liste
        results = response.get("results", [])
        restaurants.extend(results)

        # Vérifier si un next_page_token est présent
        next_page_token = response.get("next_page_token")
        if not next_page_token:
            break

        # Attendre avant de faire une nouvelle requête
        time.sleep(2)
        params = {"pagetoken": next_page_token, "key": api_key}

    return restaurants

def generate_grid_coordinates(lat, lng, sw_lat, sw_lng, ne_lat, ne_lng, step=0.005):
    """Génère une grille de points à partir des coordonnées SW et NE."""
    grid_points = []
    lat_start = sw_lat
    lat_end = ne_lat
    lng_start = sw_lng
    lng_end = ne_lng

    lat = lat_start
    while lat <= lat_end:
        lng = lng_start
        while lng <= lng_end:
            grid_points.append((lat, lng))
            lng += step
        lat += step
    return grid_points

def divide_area_and_fetch(api_key, center_lat, center_lng, radius=5000, step=0.005):
    """Divise la zone en une grille et récupère les restaurants à chaque point."""
    # Calculer les coordonnées des coins de la zone d'intérêt
    lat_step = step  # Environ 555 m pour 0.005 degré de latitude
    lng_step = step  # Environ 555 m pour 0.005 degré de longitude (plus ou moins)
    
    # Calculer les coordonnées du coin sud-ouest et nord-est
    sw_lat = center_lat - (radius / 111320)  # Sud-ouest
    sw_lng = center_lng - (radius / 40008000 * 360)
    ne_lat = center_lat + (radius / 111320)  # Nord-est
    ne_lng = center_lng + (radius / 40008000 * 360)

    # Générer les points de la grille
    grid_points = generate_grid_coordinates(center_lat, center_lng, sw_lat, sw_lng, ne_lat, ne_lng, step)

    all_restaurants = {}

    for point in grid_points:
        location = f"{point[0]},{point[1]}"
        print(f"Recherche autour de {location}...")
        restaurants = fetch_restaurants_in_area(api_key, location, radius=1000)
        for restaurant in restaurants:
            all_restaurants[restaurant["place_id"]] = restaurant  # Utiliser place_id pour éviter les doublons

    return list(all_restaurants.values())

# Recherche des restaurants à Lyon
center_lat, center_lng = 45.7568, 4.8509  # Coordonnées de Lyon
restaurants = divide_area_and_fetch(API_KEY, center_lat, center_lng, radius=5000, step=0.005)

# Sauvegarder les résultats dans un fichier JSON
with open(OUTPUT_FILE, "w", encoding="utf-8") as file:
    json.dump(restaurants, file, ensure_ascii=False, indent=4)

print(f"Nombre total de restaurants uniques : {len(restaurants)}")
print(f"Données sauvegardées dans {OUTPUT_FILE}")
