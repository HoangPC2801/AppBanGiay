import os
import firebase_admin
from firebase_admin import credentials, messaging

BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
SERVICE_ACCOUNT_PATH = os.path.join(BASE_DIR, "firebase-service-account.json")

if not firebase_admin._apps:
    cred = credentials.Certificate(SERVICE_ACCOUNT_PATH)
    firebase_admin.initialize_app(cred)


def send_push_notification(
    token: str,
    title: str,
    body: str,
    data: dict = None
):
    message = messaging.Message(
        notification=messaging.Notification(
            title=title,
            body=body
        ),
        data=data or {},
        token=token
    )

    return messaging.send(message)