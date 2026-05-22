from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session, joinedload
from typing import List
from .. import models, schemas, database
from ..database import get_db

router = APIRouter(
    prefix="/products",
    tags=["Products"] 
)

@router.get("/", response_model=List[schemas.Product])
def get_all_products(db: Session = Depends(database.get_db)):
    products = (
        db.query(models.Product)
        .options(joinedload(models.Product.variants))
        .all()
    )
    return products

@router.get("/{product_id}", response_model=schemas.Product)
def get_product(product_id: int, db: Session = Depends(get_db)):
    product = (
        db.query(models.Product)
        .options(joinedload(models.Product.variants))
        .filter(models.Product.id == product_id)
        .first()
    )

    if not product:
        raise HTTPException(status_code=404, detail="Product not found")

    return product

@router.post("/", response_model=schemas.ProductResponse)
def create_product(product: schemas.ProductCreate, db: Session = Depends(get_db)):
    product_data = product.model_dump(exclude={"variants"})
    variants_data = product.variants or []

    new_product = models.Product(**product_data)

    db.add(new_product)
    db.commit()
    db.refresh(new_product)

    for variant in variants_data:
        new_variant = models.ProductVariant(
            product_id=new_product.id,
            color=variant.color,
            size=variant.size,
            stock_quantity=variant.stock_quantity or 0
        )
        db.add(new_variant)

    db.commit()
    db.refresh(new_product)

    return new_product

@router.delete("/{product_id}")
def delete_product(product_id: int, db: Session = Depends(database.get_db)):
    # 1. Tìm sản phẩm trong Database
    product_query = db.query(models.Product).filter(models.Product.id == product_id)
    product = product_query.first()
    
    # 2. Nếu không có sản phẩm nào có ID này -> Báo lỗi
    if not product:
        raise HTTPException(status_code=404, detail="Không tìm thấy sản phẩm cần xóa")
    
    # 3. Tiến hành xóa và lưu thay đổi vào DB
    product_query.delete(synchronize_session=False)
    db.commit()
    
    return {"message": f"Đã xóa thành công sản phẩm có ID: {product_id}"}

@router.put("/{product_id}", response_model=schemas.Product)
def update_product(product_id: int, product: schemas.ProductUpdate, db: Session = Depends(get_db)):
    db_product = db.query(models.Product).filter(models.Product.id == product_id).first()

    if not db_product:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Không tìm thấy sản phẩm"
        )

    update_data = product.model_dump(exclude_unset=True, exclude={"variants"})

    for key, value in update_data.items():
        setattr(db_product, key, value)

    if product.variants is not None:
        db.query(models.ProductVariant).filter(
            models.ProductVariant.product_id == product_id
        ).delete()

        for variant in product.variants:
            new_variant = models.ProductVariant(
                product_id=product_id,
                color=variant.color,
                size=variant.size,
                stock_quantity=variant.stock_quantity or 0
            )
            db.add(new_variant)

    db.commit()
    db.refresh(db_product)

    return db_product