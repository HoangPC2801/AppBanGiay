from sqlalchemy import Column, Integer, String, Float, ForeignKey, Text, Boolean, TIMESTAMP, text, DECIMAL, Enum, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from .database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(50), unique=True, index=True)
    password = Column(String(255))
    email = Column(String(100), unique=True, index=True)
    full_name = Column(String(100))
    address = Column(Text)
    phone = Column(String(20))
    is_active = Column(Boolean, default=True)
    
    # Quan hệ với bảng Orders
    orders = relationship("Order", back_populates="owner")

class Category(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), unique=True, index=True)
    description = Column(Text)
    
    # Quan hệ 1-N với Products
    products = relationship("Product", back_populates="category_rel")

class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    name = Column(String(100), nullable=False)
    description = Column(Text, nullable=True)
    price = Column(DECIMAL(10, 2), nullable=False)
    original_price = Column(DECIMAL(10, 2), nullable=False, default=0)
    discount_percent = Column(Integer, nullable=False, default=0)
    image = Column(String(1000), nullable=True) 
    
    category = Column(String(50), nullable=True, index=True)
    category_id = Column(Integer, ForeignKey("categories.id"), nullable=True)
    category_rel = relationship("Category", back_populates="products")
    brand = Column(String(50), nullable=True, index=True)
    
    # Tự động lấy giờ hệ thống khi tạo và cập nhật
    created_at = Column(DateTime, default=func.now(), nullable=False)
    updated_at = Column(TIMESTAMP, server_default=func.now(), onupdate=func.now())
    
    # Các thuộc tính chi tiết của giày
    color = Column(String(50), nullable=True)
    stock_quantity = Column(Integer, default=0)
    material = Column(String(100), nullable=True)
    images = relationship(
    "ProductImage",
    back_populates="product",
    cascade="all, delete-orphan"
    )
    
    # Kiểu Enum cho giới tính
    gender = Column(Enum('Nam', 'Nữ', 'Unisex'), default='Unisex')
    season = Column(String(50), nullable=True)
    style = Column(String(100), nullable=True)
    
    # Trong SQLAlchemy, Boolean sẽ tự động map với tinyint(1) trong MySQL
    is_active = Column(Boolean, default=True)

    variants = relationship(
    "ProductVariant",
    back_populates="product",
    cascade="all, delete-orphan"
    )

class ProductVariant(Base):
    __tablename__ = "product_variants"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    product_id = Column(Integer, ForeignKey("products.id"), nullable=False)
    color = Column(String(50), nullable=True)
    size = Column(String(10), nullable=True)
    stock_quantity = Column(Integer, default=0)

    product = relationship("Product", back_populates="variants")

class Order(Base):
    __tablename__ = "orders"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    firebase_uid = Column(String(128), nullable=True)
    total = Column(Float)
    status = Column(String(20), default="pending")
    shipping_address = Column(Text)
    payment_method = Column(String(50))
    created_at = Column(TIMESTAMP, server_default=text("CURRENT_TIMESTAMP"))

    owner = relationship("User", back_populates="orders")
    items = relationship("OrderDetail", back_populates="order")

class OrderDetail(Base):
    __tablename__ = "order_items" 

    id = Column(Integer, primary_key=True, index=True)
    order_id = Column(Integer, ForeignKey("orders.id"))
    product_id = Column(Integer, ForeignKey("products.id"))
    quantity = Column(Integer)
    price = Column(Float)
    color = Column(String(50), nullable=True)
    size = Column(String(10), nullable=True)

    order = relationship("Order", back_populates="items")
    product = relationship("Product")

class Cart(Base):
    __tablename__ = "cart"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    product_id = Column(Integer, ForeignKey("products.id"))
    quantity = Column(Integer, default=1)
    color = Column(String(50))
    size = Column(String(10))
    
    product = relationship("Product")

class Admin(Base):
    __tablename__ = "admins"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(50), unique=True, index=True, nullable=False)
    password_hash = Column(String(255), nullable=False) # Chú ý: Cột này lưu mật khẩu đã mã hóa
    full_name = Column(String(100), nullable=False)
    role = Column(String(20), nullable=False, default="manager") # Phân quyền: 'manager' hoặc 'superadmin'

class ProductReview(Base):
    __tablename__ = "product_reviews"

    id = Column(Integer, primary_key=True, index=True)

    product_id = Column(
        Integer,
        ForeignKey("products.id", ondelete="CASCADE"),
        nullable=False
    )

    firebase_uid = Column(String(128), nullable=False)

    user_name = Column(String(255), nullable=True)

    rating = Column(Integer, nullable=False)

    comment = Column(Text, nullable=True)

    review_image = Column(String(500), nullable=True)

    is_hidden = Column(Boolean, default=False)

    admin_reply = Column(Text, nullable=True)

    admin_reply_at = Column(DateTime, nullable=True)

    created_at = Column(DateTime, server_default=func.now())

    updated_at = Column(
        DateTime,
        server_default=func.now(),
        onupdate=func.now()
    )

class ReviewLike(Base):
    __tablename__ = "review_likes"

    id = Column(Integer, primary_key=True, index=True)

    review_id = Column(
        Integer,
        ForeignKey("product_reviews.id", ondelete="CASCADE"),
        nullable=False
    )

    firebase_uid = Column(String(128), nullable=False)

    created_at = Column(DateTime, server_default=func.now())

class ProductImage(Base):
    __tablename__ = "product_images"

    id = Column(Integer, primary_key=True, index=True)
    product_id = Column(Integer, ForeignKey("products.id", ondelete="CASCADE"), nullable=False)
    image_url = Column(String(1000), nullable=False)
    sort_order = Column(Integer, default=0)

    product = relationship("Product", back_populates="images")