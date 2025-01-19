import json
import os

treated_data_path = os.path.join(".", "get_restaurants_data", "data", "restaurants_treated_data.json")

# Charger les fichiers JSON
all_restaurants_json = os.path.join('.', 'get_restaurants_data', 'data', 'all_restaurants_lyon.json')
with open(all_restaurants_json, 'r', encoding='utf-8') as f:
    all_restaurants = json.load(f)

restaurant_details_json = os.path.join('.', 'get_restaurants_data', 'data', 'restaurant_details.json')
with open(restaurant_details_json, 'r', encoding='utf-8') as f:
    restaurant_details = json.load(f)

restaurant_types_json = os.path.join('.', 'get_restaurants_data', 'data', 'restaurant_types.json')
with open(restaurant_types_json, 'r', encoding='utf-8') as f:
    restaurant_types = json.load(f)

# Créer un dictionnaire pour stocker les données traitées
# Load the JSON data
    if os.path.exists(treated_data_path):
        with open(treated_data_path, 'r', encoding='utf-8') as file:
            restaurants_treated_data = json.load(file)
    else:
        restaurants_treated_data = {}

# Parcourir tous les restaurants du fichier all_restaurants_lyon.json
for restaurant in all_restaurants:
    if restaurant['business_status'] == 'OPERATIONAL':

        place_id = restaurant['place_id']
        name = restaurant['name']
        if 'rating' in restaurant:
            rating = restaurant['rating']
        if 'user_ratings_total' in restaurant:
            user_ratings_number = restaurant['user_ratings_total']
        location = restaurant['geometry']['location']
        
        opening_hours = restaurant_details.get(place_id, {}).get('opening_hours', {}).get('weekday_text', [])
        address = restaurant_details.get(place_id, {}).get('formatted_address', None)
        
        types = restaurant_types.get(place_id, {}).get('types', [])

        # Créer un dictionnaire pour ce restaurant
        restaurant_data = {
            'place_id': place_id,
            'name': name,
            'rating': rating,
            'user_ratings_number': user_ratings_number,
            'location': location,
            'address': address,
            'opening_hours': opening_hours,
            'types': types
        }

        # Ajouter ce restaurant au dictionnaire final
        restaurants_treated_data[place_id] = restaurant_data

# Sauvegarder le fichier JSON traité
with open(treated_data_path, 'w', encoding='utf-8') as f:
    json.dump(restaurants_treated_data, f, indent=4, ensure_ascii=False)

print(f"Fichier {treated_data_path} créé avec succès.")
