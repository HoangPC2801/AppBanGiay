from pydantic import BaseModel, EmailStr
from typing import List, Optional
from datetime import datetime

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
class ProductVariantBase(BaseModel):
    color: Optional[str] = None
    size: Optional[str] = None
    stock_quantity: Optional[int] = 0

class ProductVariantCreate(ProductVariantBase):
    pass

class ProductVariantOut(ProductVariantBase):
    id: int
    product_id: int

    class Config:
        from_attributes = True

class ProductImageCreate(BaseModel):
    image_url: str
    sort_order: Optional[int] = 0


class ProductImageOut(ProductImageCreate):
    id: int
    product_id: int

    class Config:
        from_attributes = True

class ProductBase(BaseModel):
    name: str
    price: float
    description: Optional[str] = None
    
    image: Optional[str] = None 
    images: Optional[List[ProductImageCreate]] = []
    category: Optional[str] = None
    category_id: Optional[int] = None

    created_at: Optional[datetime] = datetime.now()

    brand: Optional[str] = None
    color: Optional[str] = None
    original_price: Optional[float] = 0
    discount_percent: Optional[int] = 0
    average_rating: Optional[float] = 5.0
    sold_count: Optional[int] = 0
    stock_quantity: Optional[int] = 0
    material: Optional[str] = None
    gender: Optional[str] = "Unisex"
    season: Optional[str] = None
    style: Optional[str] = None
    is_active: Optional[bool] = True


class Product(ProductBase):
    id: int
    category_rel: Optional[Category] = None
    variants: List[ProductVariantOut] = []
    images: List[ProductImageOut] = []

    class Config:
        from_attributes = True


# Khuôn dùng để nhận dữ liệu khi Tạo mới
class ProductCreate(ProductBase):
    variants: Optional[List[ProductVariantCreate]] = []

# Khuôn dùng để Trả dữ liệu về cho Web/App
class ProductResponse(ProductBase):
    id: int
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None
    variants: List[ProductVariantOut] = []
    images: List[ProductImageOut] = []

    class Config:
        from_attributes = True

class ProductUpdate(BaseModel):
    name: Optional[str] = None
    description: Optional[str] = None
    price: Optional[float] = None
    original_price: Optional[float] = None
    discount_percent: Optional[int] = None
    category: Optional[str] = None
    category_id: Optional[int] = None
    image: Optional[str] = None
    brand: Optional[str] = None
    material: Optional[str] = None
    gender: Optional[str] = None
    season: Optional[str] = None
    style: Optional[str] = None
    is_active: Optional[bool] = None
    variants: Optional[List[ProductVariantCreate]] = None
    images: Optional[List[ProductImageCreate]] = None


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
    color: Optional[str] = None
    size: Optional[str] = None

class OrderItemOut(OrderItemBase):
    id: int
    # Sử dụng class Product (đã khai báo ở trên) để lồng thông tin tên, hình ảnh,...
    product: Optional[Product] = None 

    class Config:
        from_attributes = True

class OrderCreate(BaseModel):
    user_id: int
    firebase_uid: Optional[str] = None
    customer_name: Optional[str] = None
    customer_email: Optional[str] = None
    note: Optional[str] = None
    total: float
    shipping_address: str
    payment_method: str
    items: List[OrderItemBase]

class OrderStatusUpdate(BaseModel):
    status: str 

class OrderOut(BaseModel):
    id: int
    user_id: int
    total: float
    status: str
    shipping_address: str
    payment_method: str
    created_at: datetime
    
    items: List[OrderItemOut] = []

    class Config:
        from_attributes = True

class UserBase(BaseModel):
    username: str
    email: EmailStr
    full_name: str
    phone: Optional[str] = None
    address: Optional[str] = None

class UserCreate(UserBase):
    password: str

class UserUpdate(UserBase):
    password: Optional[str] = None 

class UserOut(UserBase):
    id: int
    is_active: bool

    class Config:
        from_attributes = True

# SCHEMAS CHO ADMIN 
class AdminBase(BaseModel):
    username: str
    full_name: str
    role: str

class AdminCreate(AdminBase):
    password: str

class AdminUpdate(AdminBase):
    password: Optional[str] = None 
class AdminOut(AdminBase):
    id: int

    class Config:
        from_attributes = True

class AdminLogin(BaseModel):
    username: str
    password: str

class ProductReviewCreate(BaseModel):
    firebase_uid: str
    user_name: Optional[str] = None
    rating: int
    comment: Optional[str] = None
    review_image: Optional[str] = None
    avatar_url: Optional[str] = None


class ProductReviewOut(BaseModel):
    id: int
    product_id: int
    firebase_uid: str
    user_name: Optional[str] = None
    rating: int
    comment: Optional[str] = None
    review_image: Optional[str] = None
    avatar_url: Optional[str] = None

    is_hidden: bool = False

    admin_reply: Optional[str] = None
    admin_reply_at: Optional[datetime] = None

    created_at: datetime
    updated_at: Optional[datetime] = None

    like_count: int = 0

    class Config:
        from_attributes = True


class ProductReviewSummary(BaseModel):
    average_rating: float
    review_count: int
    sold_count: int
    page: int = 1
    limit: int = 10
    reviews: List[ProductReviewOut]

class ProductReviewReplyUpdate(BaseModel):
    reply: str

class AppBannerCreate(BaseModel):
    image_url: str
    title: Optional[str] = None
    is_active: bool = True
    display_order: int = 0

class AppBannerOut(AppBannerCreate):
    id: int

    class Config:
        from_attributes = True

class NotificationCreate(BaseModel):
    firebase_uid: Optional[str] = None
    title: str
    message: str
    type: str = "system"
    related_order_id: Optional[int] = None


class NotificationOut(NotificationCreate):
    id: int
    is_read: bool
    created_at: datetime

    class Config:
        from_attributes = True

class FcmTokenCreate(BaseModel):
    firebase_uid: str
    token: str