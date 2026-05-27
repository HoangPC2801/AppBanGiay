from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session, joinedload
from typing import Optional
from .. import models, database, schemas

router = APIRouter(prefix="/orders", tags=["Orders"])

@router.post("/")
def create_order(order: schemas.OrderCreate, db: Session = Depends(database.get_db)):
    db_order = models.Order(
        user_id=order.user_id,
        firebase_uid=order.firebase_uid,
        customer_name=order.customer_name,
        customer_email=order.customer_email,
        total=order.total,
        status="pending",
        shipping_address=order.shipping_address,
        payment_method=order.payment_method
    )

    db.add(db_order)
    db.commit()
    db.refresh(db_order)

    for item in order.items:
        db_item = models.OrderDetail(
            order_id=db_order.id,
            product_id=item.product_id,
            quantity=item.quantity,
            price=item.price,
            color=item.color,
            size=item.size
        )
        db.add(db_item)

    db.commit()

    return {
        "message": "Đặt hàng thành công",
        "order_id": db_order.id
    }

# 1. Lấy danh sách kèm theo lọc trạng thái
@router.get("/")
def get_orders(status: Optional[str] = None, db: Session = Depends(database.get_db)):
    query = db.query(models.Order, models.User.username)\
              .join(models.User, models.Order.user_id == models.User.id)\
              .options(joinedload(models.Order.items).joinedload(models.OrderDetail.product))
              
    if status:
        query = query.filter(models.Order.status == status)
        
    orders_data = query.order_by(models.Order.created_at.desc()).all()
    
    result = []
    for order, username in orders_data:
        items_data = []
        for item in order.items:
            items_data.append({
                "quantity": item.quantity,
                "price": item.price,
                "color": item.color,
                "size": item.size,
                "product": {
                    "id": item.product.id if item.product else None,
                    "name": item.product.name if item.product else "Sản phẩm lỗi/Đã xóa",
                    "image": item.product.image if item.product else None
                }
            })
            
        # Đóng gói dữ liệu trả về cho Frontend
        result.append({
            "id": order.id,
            "created_at": order.created_at,
            "total": order.total,
            "status": order.status,
            "username": username,
            "customer_name": order.customer_name,
            "customer_email": order.customer_email,
            "shipping_address": order.shipping_address,
            "payment_method": order.payment_method,
            "items": items_data 
        })
    return result

# 2. Cập nhật trạng thái / Hủy đơn hàng
@router.put("/{order_id}/status")
def update_order_status(order_id: int, status: str, db: Session = Depends(database.get_db)):
    db_order = db.query(models.Order)\
        .options(joinedload(models.Order.items))\
        .filter(models.Order.id == order_id)\
        .first()

    if not db_order:
        raise HTTPException(status_code=404, detail="Không tìm thấy đơn hàng")

    trang_thai_cu = db_order.status
    trang_thai_moi = status

    # Nếu trạng thái không đổi thì không làm gì thêm
    if trang_thai_cu == trang_thai_moi:
        return {"message": "Trạng thái không thay đổi"}

    try:
        # 1. Chuyển từ trạng thái khác sang completed => trừ tồn kho
        if trang_thai_cu != "completed" and trang_thai_moi == "completed":
            for item in db_order.items:
                tru_ton_kho(db, item)

        # 2. Chuyển từ completed sang trạng thái khác => cộng tồn kho lại
        if trang_thai_cu == "completed" and trang_thai_moi != "completed":
            for item in db_order.items:
                cong_lai_ton_kho(db, item)

        db_order.status = trang_thai_moi
        db.commit()

        return {"message": "Cập nhật trạng thái thành công"}

    except HTTPException:
        db.rollback()
        raise

    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=500, detail=str(e))

def tru_ton_kho(db: Session, item: models.OrderDetail):
    # Ưu tiên trừ tồn kho theo biến thể màu + size
    variant = None

    if item.color and item.size:
        variant = db.query(models.ProductVariant).filter(
            models.ProductVariant.product_id == item.product_id,
            models.ProductVariant.color == item.color,
            models.ProductVariant.size == item.size
        ).first()

    if variant:
        if variant.stock_quantity < item.quantity:
            raise HTTPException(
                status_code=400,
                detail=f"Sản phẩm ID {item.product_id} không đủ tồn kho"
            )

        variant.stock_quantity -= item.quantity
    else:
        product = db.query(models.Product).filter(
            models.Product.id == item.product_id
        ).first()

        if not product:
            raise HTTPException(
                status_code=404,
                detail=f"Không tìm thấy sản phẩm ID {item.product_id}"
            )

        if product.stock_quantity < item.quantity:
            raise HTTPException(
                status_code=400,
                detail=f"Sản phẩm {product.name} không đủ tồn kho"
            )

        product.stock_quantity -= item.quantity


def cong_lai_ton_kho(db: Session, item: models.OrderDetail):
    # Ưu tiên cộng lại tồn kho theo biến thể màu + size
    variant = None

    if item.color and item.size:
        variant = db.query(models.ProductVariant).filter(
            models.ProductVariant.product_id == item.product_id,
            models.ProductVariant.color == item.color,
            models.ProductVariant.size == item.size
        ).first()

    if variant:
        variant.stock_quantity += item.quantity
    else:
        product = db.query(models.Product).filter(
            models.Product.id == item.product_id
        ).first()

        if product:
            product.stock_quantity += item.quantity

@router.get("/{order_id}")
def get_order_detail(order_id: int, db: Session = Depends(database.get_db)):
    order = db.query(models.Order)\
              .options(
                  joinedload(models.Order.items).joinedload(models.OrderDetail.product)
              )\
              .filter(models.Order.id == order_id)\
              .first()
              
    if not order:
        raise HTTPException(status_code=404, detail="Không tìm thấy đơn hàng")
    return order