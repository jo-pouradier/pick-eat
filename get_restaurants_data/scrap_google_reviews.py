from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
import json
import random
import time

def get_google_reviews(place_id, num_reviews=50):
    # Configure Selenium
    chrome_options = Options()
    chrome_options.add_argument("--headless")  # Run in headless mode
    chrome_service = Service(r"C:\dev\chromedriver-win64\chromedriver.exe")
    driver = webdriver.Chrome(service=chrome_service, options=chrome_options)

    reviews = []
    try:
        # Load the Google Maps page for the place
        url = f"https://www.google.com/maps/place/?q=place_id:{place_id}"
        driver.get(url)
        time.sleep(5)  # Wait for the page to load

        # Scroll to load reviews
        for _ in range(5):  # Adjust the range to load more reviews
            driver.execute_script("window.scrollBy(0, 1000);")
            time.sleep(2)

        # Extract reviews
        review_elements = driver.find_elements(By.CLASS_NAME, "review-dialog-list-item")  # Update class name if necessary
        random.shuffle(review_elements)

        for review_element in review_elements[:num_reviews]:
            try:
                review_text = review_element.find_element(By.CLASS_NAME, "review-text").text  # Update class name if necessary
                reviews.append(review_text)
            except:
                continue
    finally:
        driver.quit()

    return reviews

def test_get_google_reviews():
    with open(r'data\all_restaurants_lyon.json', 'r', encoding='utf-8') as file:
        restaurants = json.load(file)
    
    test_reviews = {}
    
    for restaurant in restaurants[:2]:  # Test with only 2 restaurants
        place_id = restaurant.get('place_id')
        if place_id:
            reviews = get_google_reviews(place_id)
            test_reviews[restaurant['name']] = reviews
    
    with open(r'data\test_restaurant_reviews.json', 'w', encoding='utf-8') as file:
        json.dump(test_reviews, file, ensure_ascii=False, indent=4)

if __name__ == "__main__":
    test_get_google_reviews()
