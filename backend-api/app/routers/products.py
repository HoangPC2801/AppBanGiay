from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from typing import List
from .. import models, schemas, database

router = APIRouter(
    prefix="/products",
    tags=["Products"] 
)

@router.get("/", response_model=List[schemas.Product])
def get_all_products(db: Session = Depends(database.get_db)):
    products = db.query(models.Product).all()
    return products