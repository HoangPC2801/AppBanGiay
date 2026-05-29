from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from .. import models, schemas, database

router = APIRouter(prefix="/fcm", tags=["FCM"])


@router.post("/token")
def save_fcm_token(
    data: schemas.FcmTokenCreate,
    db: Session = Depends(database.get_db)
):
    existing = db.query(models.FcmToken).filter(
        models.FcmToken.firebase_uid == data.firebase_uid,
        models.FcmToken.token == data.token
    ).first()

    if existing:
        return {"message": "Token đã tồn tại"}

    new_token = models.FcmToken(
        firebase_uid=data.firebase_uid,
        token=data.token
    )

    db.add(new_token)
    db.commit()

    return {"message": "Đã lưu FCM token"}