from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from sqlalchemy import func
from .. import models, schemas, database
from datetime import datetime

router = APIRouter(prefix="/products", tags=["Product Reviews"])


@router.get(
    "/{product_id}/reviews",
    response_model=schemas.ProductReviewSummary
)
def get_product_reviews(
    product_id: int,
    page: int = Query(1, ge=1),
    limit: int = Query(10, ge=1, le=50),
    db: Session = Depends(database.get_db)
):
    offset = (page - 1) * limit

    reviews_query = (
        db.query(models.ProductReview)
        .filter(
            models.ProductReview.product_id == product_id,
            models.ProductReview.is_hidden == False
        )
        .order_by(models.ProductReview.id.desc())
    )

    reviews = (
        reviews_query
        .offset(offset)
        .limit(limit)
        .all()
    )

    avg_rating = (
        db.query(func.avg(models.ProductReview.rating))
        .filter(
            models.ProductReview.product_id == product_id,
            models.ProductReview.is_hidden == False
        )
        .scalar()
    )

    sold_count = (
        db.query(func.sum(models.OrderDetail.quantity))
        .join(
            models.Order,
            models.OrderDetail.order_id == models.Order.id
        )
        .filter(
            models.OrderDetail.product_id == product_id,
            models.Order.status == "completed"
        )
        .scalar()
    ) or 0

    result_reviews = []

    for review in reviews:
        like_count = (
            db.query(models.ReviewLike)
            .filter(models.ReviewLike.review_id == review.id)
            .count()
        )

        result_reviews.append({
            "id": review.id,
            "product_id": review.product_id,
            "firebase_uid": review.firebase_uid,
            "user_name": review.user_name,
            "rating": review.rating,
            "comment": review.comment,
            "review_image": review.review_image,
            "is_hidden": review.is_hidden,
            "admin_reply": review.admin_reply,
            "admin_reply_at": review.admin_reply_at,
            "created_at": review.created_at,
            "updated_at": review.updated_at,
            "like_count": like_count
        })

    return {
        "average_rating": round(float(avg_rating or 5), 1),
        "review_count": reviews_query.count(),
        "sold_count": int(sold_count),
        "page": page,
        "limit": limit,
        "reviews": result_reviews
    }


@router.post(
    "/{product_id}/reviews",
    response_model=schemas.ProductReviewOut
)
def create_product_review(
    product_id: int,
    review: schemas.ProductReviewCreate,
    db: Session = Depends(database.get_db)
):
    product = (
        db.query(models.Product)
        .filter(models.Product.id == product_id)
        .first()
    )

    if not product:
        raise HTTPException(
            status_code=404,
            detail="Product not found"
        )

    has_bought = (
        db.query(models.OrderDetail)
        .join(
            models.Order,
            models.OrderDetail.order_id == models.Order.id
        )
        .filter(
            models.Order.firebase_uid == review.firebase_uid,
            models.Order.status == "completed",
            models.OrderDetail.product_id == product_id
        )
        .first()
    )

    if not has_bought:
        raise HTTPException(
            status_code=403,
            detail="Bạn cần mua sản phẩm trước khi đánh giá"
        )

    existing = (
        db.query(models.ProductReview)
        .filter(
            models.ProductReview.product_id == product_id,
            models.ProductReview.firebase_uid == review.firebase_uid
        )
        .first()
    )

    if existing:
        if existing.updated_at:
            time_diff = datetime.utcnow() - existing.updated_at

            if time_diff.total_seconds() < 300:
                raise HTTPException(
                    status_code=429,
                    detail="Bạn chỉ có thể sửa đánh giá sau 5 phút"
                )

        existing.user_name = review.user_name
        existing.rating = review.rating
        existing.comment = review.comment
        existing.review_image = review.review_image

        db.commit()
        db.refresh(existing)

        return existing

    new_review = models.ProductReview(
        product_id=product_id,
        firebase_uid=review.firebase_uid,
        user_name=review.user_name,
        rating=review.rating,
        comment=review.comment,
        review_image=review.review_image
    )

    db.add(new_review)

    db.commit()

    db.refresh(new_review)

    return new_review

@router.get("/reviews/admin/all")
def get_all_reviews(db: Session = Depends(database.get_db)):
    reviews = (
        db.query(models.ProductReview, models.Product)
        .join(models.Product, models.ProductReview.product_id == models.Product.id)
        .order_by(models.ProductReview.id.desc())
        .all()
    )

    return [
        {
            "id": review.id,
            "product_id": review.product_id,
            "product_name": product.name,
            "firebase_uid": review.firebase_uid,
            "user_name": review.user_name,
            "rating": review.rating,
            "comment": review.comment,
            "created_at": review.created_at
        }
        for review, product in reviews
    ]


@router.delete("/reviews/admin/{review_id}")
def delete_review(review_id: int, db: Session = Depends(database.get_db)):
    review = db.query(models.ProductReview).filter(models.ProductReview.id == review_id).first()

    if not review:
        raise HTTPException(status_code=404, detail="Review not found")

    db.delete(review)
    db.commit()

    return {"message": "Deleted"}

@router.patch("/reviews/admin/{review_id}/hide")
def hide_review(
    review_id: int,
    db: Session = Depends(database.get_db)
):
    review = (
        db.query(models.ProductReview)
        .filter(models.ProductReview.id == review_id)
        .first()
    )

    if not review:
        raise HTTPException(
            status_code=404,
            detail="Review not found"
        )

    review.is_hidden = True

    db.commit()

    return {"message": "Review hidden"}

@router.patch("/reviews/admin/{review_id}/show")
def show_review(
    review_id: int,
    db: Session = Depends(database.get_db)
):
    review = (
        db.query(models.ProductReview)
        .filter(models.ProductReview.id == review_id)
        .first()
    )

    if not review:
        raise HTTPException(
            status_code=404,
            detail="Review not found"
        )

    review.is_hidden = False

    db.commit()

    return {"message": "Review shown"}

@router.post("/reviews/{review_id}/like")
def like_review(
    review_id: int,
    firebase_uid: str,
    db: Session = Depends(database.get_db)
):
    existing = (
        db.query(models.ReviewLike)
        .filter(
            models.ReviewLike.review_id == review_id,
            models.ReviewLike.firebase_uid == firebase_uid
        )
        .first()
    )

    if existing:
        db.delete(existing)
        db.commit()

        return {
            "liked": False
        }

    new_like = models.ReviewLike(
        review_id=review_id,
        firebase_uid=firebase_uid
    )

    db.add(new_like)

    db.commit()

    return {
        "liked": True
    }

@router.patch("/reviews/admin/{review_id}/reply")
def reply_review(
    review_id: int,
    data: schemas.ProductReviewReplyUpdate,
    db: Session = Depends(database.get_db)
):
    review = (
        db.query(models.ProductReview)
        .filter(models.ProductReview.id == review_id)
        .first()
    )

    if not review:
        raise HTTPException(
            status_code=404,
            detail="Review not found"
        )

    review.admin_reply = data.reply
    review.admin_reply_at = datetime.utcnow()

    db.commit()

    return {
        "message": "Replied"
    }