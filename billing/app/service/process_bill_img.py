import io
import os

import imutils
import cv2
from imutils.perspective import four_point_transform
import numpy
import pytesseract

from ..broker import BrokerManager

from ..db.models import BillModel


def process_bill_img(bill: BillModel, image_io: io.BytesIO | None = None):
    # Download the image from the bucket
    image = io.BytesIO()
    if image_io is None:
        with open(bill.path, "rb") as f:
            image.write(f.read())
        image.seek(0)
    else:
        image = image_io
    image.seek(0)

    image_bytes = numpy.frombuffer(bytearray(image.read()), dtype=numpy.uint8)

    # Extract text from the image
    text = extract_text_from_receipt(image_bytes)
    BrokerManager.send_bill_text_to_process(text, bill.id)


def extract_text_from_receipt(image_data: numpy.ndarray) -> str:
    # Load and preprocess the image
    img_orig = cv2.imdecode(image_data, cv2.IMREAD_COLOR)
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
    text = pytesseract.image_to_string(
        cv2.cvtColor(receipt, cv2.COLOR_BGR2RGB), config=options
    )

    return text
