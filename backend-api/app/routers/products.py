from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session, joinedload
from sqlalchemy import func
from typing import List
from .. import models, schemas, database
from ..database import get_db

router = APIRouter(
    prefix="/products",
    tags=["Products"] 
)

def tinh_giam_gia(price, original_price):
    price = float(price or 0)
    original_price = float(original_price or 0)

    if original_price <= 0:
        original_price = price

    if original_price > price and price > 0:
        discount_percent = round(
            100 - (price / original_price * 100)
        )
    else:
        discount_percent = 0

    return original_price, discount_percent

@router.get("/", response_model=List[schemas.Product])
def get_all_products(db: Session = Depends(database.get_db)):
    products = (
        db.query(models.Product)
        .options(
            joinedload(models.Product.variants),
            joinedload(models.Product.images)
        )
        .all()
    )
    for product in products:

        avg_rating = (
            db.query(func.avg(models.ProductReview.rating))
            .filter(
                models.ProductReview.product_id == product.id,
                models.ProductReview.is_hidden == False
            )
            .scalar()
        )

        product.average_rating = round(avg_rating or 5.0, 1)

        sold_count = (
            db.query(func.sum(models.OrderDetail.quantity))
            .filter(models.OrderDetail.product_id == product.id)
            .scalar()
        )

        product.sold_count = sold_count or 0

    return products

@router.get("/{product_id}", response_model=schemas.Product)
def get_product(product_id: int, db: Session = Depends(get_db)):
    product = (
        db.query(models.Product)
        .options(
            joinedload(models.Product.variants),
            joinedload(models.Product.images)
        )
        .filter(models.Product.id == product_id)
        .first()
    )

    if not product:
        raise HTTPException(status_code=404, detail="Product not found")

    avg_rating = (
        db.query(func.avg(models.ProductReview.rating))
        .filter(
            models.ProductReview.product_id == product.id,
            models.ProductReview.is_hidden == False
        )
        .scalar()
    )

    product.average_rating = round(avg_rating or 5.0, 1)

    sold_count = (
        db.query(func.sum(models.OrderDetail.quantity))
        .filter(models.OrderDetail.product_id == product.id)
        .scalar()
    )

    product.sold_count = sold_count or 0

    return product

@router.post("/", response_model=schemas.ProductResponse)
def create_product(product: schemas.ProductCreate, db: Session = Depends(get_db)):
    product_data = product.model_dump(
        exclude={"variants", "images"}
    )
    variants_data = product.variants or []
    images_data = product.images or []

    original_price, discount_percent = tinh_giam_gia(
        price=product_data.get("price"),
        original_price=product_data.get("original_price")
    )

    product_data["original_price"] = original_price
    product_data["discount_percent"] = discount_percent

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

    for index, img in enumerate(images_data):

        new_image = models.ProductImage(
            product_id=new_product.id,
            image_url=img.image_url,
            sort_order=img.sort_order or index
        )

        db.add(new_image)

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

    update_data = product.model_dump(
        exclude_unset=True,
        exclude={"variants", "images"}
    )

    for key, value in update_data.items():
        setattr(db_product, key, value)

    price_hien_tai = getattr(db_product, "price", 0)
    gia_goc_hien_tai = getattr(db_product, "original_price", 0)

    original_price, discount_percent = tinh_giam_gia(
        price=price_hien_tai,
        original_price=gia_goc_hien_tai
    )

    db_product.original_price = original_price
    db_product.discount_percent = discount_percent

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

    if product.images is not None:

        db.query(models.ProductImage).filter(
            models.ProductImage.product_id == product_id
        ).delete()

        for index, img in enumerate(product.images):

            new_image = models.ProductImage(
                product_id=product_id,
                image_url=img.image_url,
                sort_order=img.sort_order or index
            )

            db.add(new_image)

    db.commit()
    db.refresh(db_product)

    return db_product