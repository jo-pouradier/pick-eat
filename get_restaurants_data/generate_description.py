import argparse
import os
import json
import cv2
import imutils
import pytesseract
from imutils.perspective import four_point_transform
import ollama
import time

pytesseract.pytesseract.tesseract_cmd = os.path.join(".", "scan_ticket_caisse", "tools", "Tesseract-OCR", "tesseract.exe")

def generate_restaurants_descriptions(input_json_path=os.path.join(".", "get_restaurants_data", "data", "restaurants_treated_data.json"), num_restaurants=None):
    # Load the JSON data
    with open(input_json_path, 'r', encoding='utf-8') as file:
        restaurants_data = json.load(file)

    count = 0

    # Extract notes and types for each restaurant
    for place_id, restaurant in restaurants_data.items():

        if num_restaurants is not None and count >= num_restaurants:
            break

        if 'description' in restaurant:
            continue

        count += 1

        name = restaurant.get('name', None)
        place_id = restaurant.get('place_id', None)
        note = restaurant.get('rating', None)
        types_list = [t for t in restaurant.get('types', []) if t not in {"food", "point_of_interest", "establishment"}]

        # Create the prompt
        prompt = (
            "Generate a short description in french for a restaurant from its following attributes:\n\n"
            + "Rating: " + str(note) + "\n"
            + "Types: " + ", ".join(types_list) + "\n\n"
            + "Use the rating to describe the quality of the restaurant and the types translated in french to describe the cuisine.\n"
            + "A rating of 4 or more should be considered as a good restaurant.\n"
            + "A rating between 3 and 4 should be considered as an average restaurant.\n"
            + "A rating below 3 should be considered as a very bad restaurant.\n"
            + "Return only the description text."
        )

        try:
            # Use Ollama's Python library to send the request
            print("Generating description for restaurant ", name, " ...")
            response = ollama.chat(
                model="qwen2.5:7b",
                messages=[
                    {
                        "role": "user",
                        "content": prompt
                    }
                ]
            )

            # Check and process the response
            if response:
                response_content = response['message']['content']
                # Save items to a JSON file
                for restaurant_id, restaurant in restaurants_data.items():
                    if restaurant_id == place_id:
                        restaurant['description'] = response_content
                        print("Description generated:", response_content)
                        break
            else:
                raise Exception("No valid response from Ollama model.")
        except Exception as e:
            print(f"Error during Ollama processing: {e}")
            return None
    # Enregistrer les données mises à jour
    with open(input_json_path, "w", encoding="utf-8") as file:
        json.dump(restaurants_data, file, ensure_ascii=False, indent=4)
    print(f"Descriptions saved to {input_json_path}.")


if __name__ == "__main__":
    start_time = time.time()
    generate_restaurants_descriptions(num_restaurants=5)
    end_time = time.time()
    execution_time = end_time - start_time
    print(f"\nTotal execution time: {execution_time:.2f} seconds")