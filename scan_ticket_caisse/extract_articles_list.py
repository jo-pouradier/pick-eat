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

def extract_text_from_receipt():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-i", "--image", type=str, required=False, help="path to input image",
        default = os.path.join(".", "scan_ticket_caisse", "data", "tickets_ex", "ninkasi.jpg")
    )
    args = parser.parse_args()

    # Check if image exists
    if not os.path.exists(args.image):
        raise Exception("The given image does not exist.")

    # Load and preprocess the image
    img_orig = cv2.imread(args.image)
    image = img_orig.copy()
    image = imutils.resize(image, width=500)
    ratio = img_orig.shape[1] / float(image.shape[1])

    # Convert to grayscale and detect edges
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    edged = cv2.Canny(blurred, 75, 200)

    # Find contours
    cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    cnts = sorted(cnts, key=cv2.contourArea, reverse=True)

    # Detect receipt contour
    receiptCnt = None
    for c in cnts:
        peri = cv2.arcLength(c, True)
        approx = cv2.approxPolyDP(c, 0.02 * peri, True)
        if len(approx) == 4:
            receiptCnt = approx
            break

    if receiptCnt is None:
        raise Exception(
            "Could not find receipt outline. "
            "Try debugging your edge detection and contour steps."
        )

    # Perspective transform
    receipt = four_point_transform(img_orig, receiptCnt.reshape(4, 2) * ratio)

    # Apply OCR
    options = "--psm 6"
    text = pytesseract.image_to_string(cv2.cvtColor(receipt, cv2.COLOR_BGR2RGB), config=options)

    print("[INFO] Raw OCR output:")
    print(text)
    print("\n")

    return text


def extract_items_from_text(text, output_path=os.path.join(".", "scan_ticket_caisse", "data", "output", "temp_receipt_items.json")):
    # Create the prompt
    prompt = (
        "Extract the list of items, their quantities, and unit prices from the following receipt text:\n\n"
        + text
        + "\n\nThe output should be a json list of items composed of the attributes name, quantity, and unit_price. Add the total_cost as the last item of the json.\n\n"
        + "Use the items prices, quantities and total cost to correct the potential errors in the provided text.\n\n"
        + "Make sure that the total cost equals the sum of the prices of all items according to their quantity.\n\n"
        + "No need for explainations, just return the json."
    )

    try:
        # Use Ollama's Python library to send the request
        print("Sending request to Ollama model...")
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

            # Extraire uniquement le JSON entre les crochets
            json_str = response_content.strip("```json\n").strip("\n```")
            start_index = json_str.find('[')  # Trouver le début du JSON
            end_index = json_str.rfind(']')  # Trouver la fin du JSON

            # Extraire la portion entre les crochets
            json_only = json_str[start_index:end_index+1]

            print("json_only: ", json_only)

            # Parse the JSON data
            items = json.loads(json_only)

            # Save items to a JSON file
            with open(output_path, 'w', encoding="utf-8") as json_file:
                json.dump(items, json_file, indent=4)
            print(f"Items extracted and saved to {output_path}.")
            return items
        else:
            raise Exception("No valid response from Ollama model.")
    except Exception as e:
        print(f"Error during Ollama processing: {e}")
        return None


if __name__ == "__main__":
    start_time = time.time()
    # Extract text from receipt image
    text = extract_text_from_receipt()
    # Extract items using Ollama
    items = extract_items_from_text(text)
    if items:
        print("Extracted Items:")
        print(items)
    else:
        print("No items were extracted.")
    end_time = time.time()
    execution_time = end_time - start_time
    print(f"\nTotal execution time: {execution_time:.2f} seconds")