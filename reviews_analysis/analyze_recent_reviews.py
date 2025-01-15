import json
from datetime import datetime, timedelta
from transformers import pipeline
from tqdm import tqdm
import os

def parse_relative_date(relative_date):
    """Convertit une date relative en objet datetime."""
    now = datetime.now()
    num_date = relative_date.split()[3]
    if num_date == "un" or num_date == "une":
        num_date = 1
    else:
        num_date = int(num_date)
    if "semaine" in relative_date:
        return now - timedelta(weeks=num_date)
    elif "jour" in relative_date:
        return now - timedelta(days=num_date)
    elif "mois" in relative_date:
        return now - timedelta(days=30 * num_date)  # Approximation d'un mois
    elif "an" in relative_date:
        return now - timedelta(days=365 * num_date)  # Approximation d'un an
    else:
        return now  # Retourne la date actuelle si le format est inconnu

def filter_recent_reviews(json_data, max_months=2):
    """
    Filtre les avis récents dans un dictionnaire JSON.
    :param json_data: Dictionnaire contenant les avis.
    :param max_months: Nombre maximum de mois pour qu'un avis soit considéré comme récent.
    :return: Un dictionnaire avec uniquement les avis récents.
    """
    now = datetime.now()
    recent_reviews = {}
    for restaurant, reviews in json_data.items():
        filtered_reviews = []
        for review in reviews:
            review_date = parse_relative_date(review["Reviewed On"])
            if (now - review_date).days <= 30 * max_months:
                filtered_reviews.append(review)
        if filtered_reviews:
            recent_reviews[restaurant] = filtered_reviews
    print(f"Nombre d'avis récents: {sum(len(reviews) for reviews in recent_reviews.values())}")
    return recent_reviews

def calculate_average_ratings_and_num_reviews(reviews):
    total_rating = 0
    num_reviews = 0
    for review in reviews:
        # Extraire la note sous forme de chiffre depuis "5 étoiles", "4 étoiles", etc.
        rating_text = review["Rating"]
        try:
            rating = int(rating_text.split()[0])  # Extrait le premier chiffre
            total_rating += rating
            num_reviews += 1
        except (ValueError, IndexError):
            # Si le format est invalide, ignorer cet avis
            continue
    if num_reviews > 0:
        average_rating = round(total_rating / num_reviews, 2)  # Arrondi à 2 décimales
    else:
        average_rating = None  # Aucun avis valide
    return average_rating, num_reviews

def calculate_sentiments_score(reviews, sentiment_analyzer):
    # Charger un pipeline de classification de sentiment
    total_score = 0
    num_reviews = 0
    for review in reviews:
        review_text = review["Review Text"]
        try:
            # Analyse du sentiment
            result = sentiment_analyzer(review_text)[0]
            score = result["score"] if result["label"] == "POSITIVE" else 1 - result["score"]
            total_score += score
            num_reviews += 1
        except Exception as e:
            # Ignorer les erreurs d'analyse
            continue
    
    if num_reviews > 0:
        average_sentiment = round(total_score / num_reviews, 2)
    else:
        average_sentiment = None  # Aucun avis valide
    return average_sentiment

def analyze_recent_reviews(recent_reviews):
    num_restaurants = len(reviews_data.keys())
    processed_restaurants = 0
    recent_reviews_analysis = {}
    sentiment_analyzer = pipeline("sentiment-analysis")
    
    # Créer une barre de progression
    with tqdm(total=num_restaurants, desc="Analyse des commentaires récents", unit="restaurant") as pbar:
        for restaurant, reviews in recent_reviews.items():
            average_sentiment = calculate_sentiments_score(reviews, sentiment_analyzer)
            average_rating, num_reviews = calculate_average_ratings_and_num_reviews(reviews)

            processed_restaurants += 1
            pbar.update(1)

            recent_reviews_analysis[restaurant] = {
                "average_sentiment_score": average_sentiment,
                "average_rating": average_rating,
                "num_reviews": num_reviews
            }

    print(f"\nAnalyse terminée : {processed_restaurants} restaurants traités.")
    return recent_reviews_analysis

def save_json_data(data, filepath=os.path.join(".", "reviews_analysis", "data", "reviews_trend.json")):
    with open(filepath, "w", encoding="utf-8") as file:
        json.dump(data, file, indent=4, ensure_ascii=False)
    print(f"Data saved to {filepath}.")


# Exemple d'utilisation
if __name__ == "__main__":

    reviews_filepath = os.path.join(".", "get_restaurants_data", "data", "reviews.json")

    # Charger le JSON d'exemple
    with open(reviews_filepath, "r", encoding="utf-8") as file:
        reviews_data = json.load(file)
    
    # Filtrer les avis récents
    recent_reviews = filter_recent_reviews(reviews_data)
    recent_reviews_analysis = analyze_recent_reviews(recent_reviews)
    save_json_data(recent_reviews_analysis)