from sqlalchemy import Column, Integer, String, Float, ForeignKey, Text, Boolean, TIMESTAMP, text
from sqlalchemy.orm import relationship
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
    products = relationship("Product", back_populates="category")

class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), index=True)
    description = Column(Text)
    price = Column(Float)
    image = Column(String(255))
    category_id = Column(Integer, ForeignKey("categories.id"))
    brand = Column(String(50))
    stock_quantity = Column(Integer, default=0)
    is_active = Column(Boolean, default=True)

    category = relationship("Category", back_populates="products")

class Order(Base):
    __tablename__ = "orders"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
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

    order = relationship("Order", back_populates="items")

class Cart(Base):
    __tablename__ = "cart"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    product_id = Column(Integer, ForeignKey("products.id"))
    quantity = Column(Integer, default=1)
    color = Column(String(50))
    size = Column(String(10))
    
    product = relationship("Product")