from pydantic import BaseModel
from typing import List, Optional

# SCHEMAS CHO CATEGORY
class CategoryBase(BaseModel):
    name: str
    description: Optional[str] = None

class CategoryCreate(CategoryBase):
    pass

class Category(CategoryBase):
    id: int

    class Config:
        from_attributes = True  

# SCHEMAS CHO PRODUCT
class ProductBase(BaseModel):
    name: str
    price: float
    description: Optional[str] = None
    image: Optional[str] = None
    category_id: int
    brand: Optional[str] = None
    stock_quantity: int = 0
    is_active: bool = True

# Schema dùng để trả dữ liệu về cho Client (App/Web)
class Product(ProductBase):
    id: int
    category: Optional[Category] = None

    class Config:
        from_attributes = True 


# SCHEMAS CHO GIỎ HÀNG
class CartItemBase(BaseModel):
    product_id: int
    quantity: int = 1
    color: Optional[str] = None
    size: Optional[str] = None

class CartItemCreate(CartItemBase):
    user_id: int 

class CartItemOut(CartItemBase):
    id: int
    user_id: int
    
    class Config:
        from_attributes = True

# SCHEMAS CHO ĐƠN HÀNG (CHECKOUT)
class OrderItemBase(BaseModel):
    product_id: int
    quantity: int
    price: float

class OrderCreate(BaseModel):
    user_id: int
    total: float
    shipping_address: str
    payment_method: str
    items: List[OrderItemBase] # Một đơn hàng chứa nhiều sản phẩm

class OrderOut(BaseModel):
    id: int
    user_id: int
    total: float
    status: str
    shipping_address: str
    payment_method: str

    class Config:
        from_attributes = True