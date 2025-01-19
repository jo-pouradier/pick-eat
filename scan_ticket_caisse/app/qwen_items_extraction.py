import argparse
import os
import json
import ollama
import time

def extract_items_from_text(frame):
    # Create the prompt
    prompt = (
        "Extract the list of items, their quantities, and unit prices from the following receipt text:\n\n"
        + frame['text']
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

            # Parse the JSON data
            items = json.loads(json_only)

            # Save items to a JSON file
            # with open(output_path, 'w', encoding="utf-8") as json_file:
            #     json.dump(items, json_file, indent=4)
            # print(f"Items extracted and saved to {output_path}.")
            return (items, frame['bill_id'])
        else:
            raise Exception("No valid response from Ollama model.")
    except Exception as e:
        print(f"Error during Ollama processing: {e}")
        return None


if __name__ == "__main__":
    output_jsnon_path = os.path.join(".", "scan_ticket_caisse", "data", "output", "temp_receipt_items.json")

    parser = argparse.ArgumentParser(description="Extract items from receipt text using Ollama.")
    parser.add_argument("--output", type=str, default=output_jsnon_path, help="Path to save the extracted items.")
    parser.add_argument("--text", type=str, required=True, help="Receipt text to extract items from.")
    args = parser.parse_args()
    start_time = time.time()
    # Extract items using Ollama
    items = extract_items_from_text(text=args.text, output_path=args.output)
    print("Items extracted:", items)
    end_time = time.time()
    execution_time = end_time - start_time
    print(f"\nTotal execution time: {execution_time:.2f} seconds")