from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional

from .. import models, schemas, database
from app.services.fcm_service import send_push_notification

router = APIRouter(prefix="/notifications", tags=["Notifications"])


@router.get("/", response_model=List[schemas.NotificationOut])
def get_notifications(
    firebase_uid: str,
    type: Optional[str] = None,
    db: Session = Depends(database.get_db)
):
    query = db.query(models.Notification).filter(
        (models.Notification.firebase_uid == firebase_uid) |
        (models.Notification.firebase_uid == None)
    )

    if type:
        query = query.filter(models.Notification.type == type)

    return query.order_by(models.Notification.created_at.desc()).all()


@router.post("/admin", response_model=schemas.NotificationOut)
def create_notification(
    notification: schemas.NotificationCreate,
    db: Session = Depends(database.get_db)
):
    new_notification = models.Notification(
        firebase_uid=notification.firebase_uid,
        title=notification.title,
        message=notification.message,
        type=notification.type,
        related_order_id=notification.related_order_id
    )

    db.add(new_notification)
    db.commit()
    db.refresh(new_notification)

    if notification.type == "promotion":
        query = db.query(models.FcmToken)

        if notification.firebase_uid:
            query = query.filter(
                models.FcmToken.firebase_uid == notification.firebase_uid
            )

        tokens = query.all()

        for token_item in tokens:
            try:
                send_push_notification(
                    token=token_item.token,
                    title=notification.title,
                    body=notification.message,
                    data={
                        "type": "promotion",
                        "notification_id": str(new_notification.id)
                    }
                )
            except Exception as e:
                print("Lỗi gửi FCM:", e)

    return new_notification


@router.patch("/{notification_id}/read")
def mark_notification_as_read(
    notification_id: int,
    db: Session = Depends(database.get_db)
):
    notification = db.query(models.Notification).filter(
        models.Notification.id == notification_id
    ).first()

    if not notification:
        raise HTTPException(status_code=404, detail="Không tìm thấy thông báo")

    notification.is_read = True
    db.commit()

    return {"message": "Đã đánh dấu đã đọc"}


@router.delete("/{notification_id}")
def delete_notification(
    notification_id: int,
    db: Session = Depends(database.get_db)
):
    notification = db.query(models.Notification).filter(
        models.Notification.id == notification_id
    ).first()

    if not notification:
        raise HTTPException(status_code=404, detail="Không tìm thấy thông báo")

    db.delete(notification)
    db.commit()

    return {"message": "Đã xóa thông báo"}

@router.get("/unread-count")
def get_unread_notification_count(
    firebase_uid: str,
    db: Session = Depends(database.get_db)
):
    count = db.query(models.Notification).filter(
        (
            (models.Notification.firebase_uid == firebase_uid) |
            (models.Notification.firebase_uid == None)
        ),
        models.Notification.is_read == False
    ).count()

    return {
        "unread_count": count
    }