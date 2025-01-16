from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import json
import time
import os

options = webdriver.ChromeOptions()
options.add_argument("--headless=new")

def getReviews(name, num_reviews=50):
    all_reviews = []
    search = name
    forUrl = search.replace(' ', '+')
    browser = webdriver.Chrome(options=options)
    browser.get(f"https://www.google.com/maps/search/{forUrl}")

    try:
        # Attendre que la page charge l'élément correspondant

        WebDriverWait(browser, 5).until(
            EC.element_to_be_clickable((By.XPATH, "//button[@aria-label='Tout accepter']"))
        ).click()
        time.sleep(0.2)

        # WebDriverWait(browser, 60).until(
        #     EC.presence_of_element_located((By.CLASS_NAME, "m6QErb.WNBkOb.XiKgde"))
        # )

        #Click on reviews
        WebDriverWait(browser, 5).until(
            EC.element_to_be_clickable((By.XPATH, "//button[@role='tab' and @data-tab-index='1']"))
        ).click()
        time.sleep(0.2)

        WebDriverWait(browser, 5).until(
            EC.element_to_be_clickable((By.XPATH, "//button[@class='g88MCb S9kvJb ' and @data-value='Trier']"))
        ).click()
        time.sleep(0.2)

        WebDriverWait(browser, 5).until(
            EC.element_to_be_clickable((By.XPATH, "//div[@class='fxNQSd' and @data-index='1']"))
        ).click()
        time.sleep(0.2)
            
        anchor = browser.find_element(By.XPATH, '//*[@id="QA0Szd"]/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]')
        reviews = anchor.find_elements(By.XPATH, "//div[@class='jftiEf fontBodyMedium ']")

        linkCount = len(reviews)

        time.sleep(1)

        # Faites défiler pour charger plus d'éléments si nécessaire
        for _ in range(10):
            browser.execute_script("arguments[0].scrollTop = arguments[0].scrollHeight", anchor)
            time.sleep(0.5)
            reviews = anchor.find_elements(By.XPATH, "//div[@class='jftiEf fontBodyMedium ']")
            if len(reviews) > linkCount and len(all_reviews) < num_reviews:
                linkCount = len(reviews)
                for review in reviews:
                    if len(all_reviews) >= num_reviews:
                        break
                    try:
                        review.find_element(By.XPATH, "//button[@class='w8nwRe kyuRq']").click()
                    except:
                        None
                    reviewer = review.find_element(By.CLASS_NAME,'d4r55 ').text
                    rating = review.find_element(By.CLASS_NAME,'kvMYJc').get_attribute('aria-label')
                    date = review.find_element(By.CLASS_NAME,'rsqaWe').text
                    try:
                        review_text = review.find_element(By.CLASS_NAME,'wiI7pd').text
                    except:
                        continue
                    if any(r['Reviewer'] == reviewer for r in all_reviews):
                        continue
                    all_reviews.append(
                        {
                            "Reviewer":reviewer,
                            "Rating":rating,
                            "Reviewed On":date,
                            "Review Text":review_text
                        }
                    )
            else:
                break  # Arrêter si aucun nouvel élément n'est chargé

    except Exception as e:
        print(f"Erreur lors de la récupération des avis : {e}")
        return []

    finally:
        browser.quit()

    return all_reviews

# def saveReviews(reviews, filepath = r".\get_restaurants_data\data\reviews.json"):
#     with open(filepath, "w", encoding="utf-8") as file:
#         json.dump(reviews, file, indent=4, ensure_ascii=False)
#     print(f"Reviews saved to {filepath}.")

def getReviewsForRestaurants(restaurant_file, num_reviews=20, num_restos=None, output_file= os.path.join(".", "get_restaurants_data", "data", "reviews.json")):
    # Charger les restaurants depuis le fichier JSON
    with open(restaurant_file, "r", encoding="utf-8") as file:
        restaurants = json.load(file)

    if num_restos is None:
        num_restos = len(restaurants)

    all_restaurants_reviews = {}
    success_count = 0
    failure_count = 0

    for idx, restaurant in enumerate(restaurants[:num_restos]):  # Limiter le nombre de restaurants
        name = restaurant["name"]
        print(f"Récupération des avis pour {name} ({idx + 1}/{num_restos})...")
        reviews = getReviews(name, num_reviews)
        if reviews:
            all_restaurants_reviews[name] = reviews
            success_count += 1
        else:
            failure_count += 1
            print(f"Aucun avis trouvé pour {name}.")

    print(f"\nRésultat : {success_count} succès, {failure_count} échecs.")

    # Sauvegarder tous les avis dans un fichier
    with open(output_file, "w", encoding="utf-8") as file:
        json.dump(all_restaurants_reviews, file, indent=4, ensure_ascii=False)

    print(f"Tous les avis ont été sauvegardés dans {output_file}.")


if __name__ == "__main__":
    input_file = os.path.join(".", "get_restaurants_data", "data", "all_restaurants_lyon.json")
    getReviewsForRestaurants(input_file, num_reviews=20)

