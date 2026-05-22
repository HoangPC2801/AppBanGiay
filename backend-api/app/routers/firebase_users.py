from fastapi import APIRouter, HTTPException
from firebase_admin import auth

from ..firebase_admin import firebase_admin

router = APIRouter(
    prefix="/firebase-users",
    tags=["Firebase Users"]
)


@router.get("/")
def get_firebase_users():
    users = []

    try:
        page = auth.list_users()

        while page:
            for user in page.users:
                users.append({
                    "uid": user.uid,
                    "email": user.email,
                    "display_name": user.display_name,
                    "phone_number": user.phone_number,
                    "photo_url": user.photo_url,
                    "disabled": user.disabled,
                    "email_verified": user.email_verified,
                    "creation_timestamp": user.user_metadata.creation_timestamp,
                    "last_sign_in_timestamp": user.user_metadata.last_sign_in_timestamp,
                })

            page = page.get_next_page()

        return users

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.patch("/{uid}/disable")
def disable_user(uid: str):
    try:
        auth.update_user(uid, disabled=True)
        return {"message": "Đã khóa tài khoản"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.patch("/{uid}/enable")
def enable_user(uid: str):
    try:
        auth.update_user(uid, disabled=False)
        return {"message": "Đã mở khóa tài khoản"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.delete("/{uid}")
def delete_user(uid: str):
    try:
        auth.delete_user(uid)
        return {"message": "Đã xóa tài khoản"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))