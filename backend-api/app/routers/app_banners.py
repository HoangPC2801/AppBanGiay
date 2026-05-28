from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .. import models, schemas, database

router = APIRouter(prefix="/app-banners", tags=["App Banners"])

@router.get("/", response_model=List[schemas.AppBannerOut])
def get_banners(db: Session = Depends(database.get_db)):
    return db.query(models.AppBanner)\
        .filter(models.AppBanner.is_active == True)\
        .order_by(models.AppBanner.display_order.asc())\
        .all()

@router.get("/admin", response_model=List[schemas.AppBannerOut])
def get_all_banners(db: Session = Depends(database.get_db)):
    return db.query(models.AppBanner)\
        .order_by(models.AppBanner.display_order.asc())\
        .all()

@router.post("/", response_model=schemas.AppBannerOut)
def create_banner(banner: schemas.AppBannerCreate, db: Session = Depends(database.get_db)):
    new_banner = models.AppBanner(**banner.dict())
    db.add(new_banner)
    db.commit()
    db.refresh(new_banner)
    return new_banner

@router.put("/{banner_id}", response_model=schemas.AppBannerOut)
def update_banner(banner_id: int, banner: schemas.AppBannerCreate, db: Session = Depends(database.get_db)):
    db_banner = db.query(models.AppBanner).filter(models.AppBanner.id == banner_id).first()

    if not db_banner:
        raise HTTPException(status_code=404, detail="Không tìm thấy banner")

    db_banner.image_url = banner.image_url
    db_banner.title = banner.title
    db_banner.is_active = banner.is_active
    db_banner.display_order = banner.display_order

    db.commit()
    db.refresh(db_banner)
    return db_banner

@router.delete("/{banner_id}")
def delete_banner(banner_id: int, db: Session = Depends(database.get_db)):
    db_banner = db.query(models.AppBanner).filter(models.AppBanner.id == banner_id).first()

    if not db_banner:
        raise HTTPException(status_code=404, detail="Không tìm thấy banner")

    db.delete(db_banner)
    db.commit()

    return {"message": "Đã xóa banner"}