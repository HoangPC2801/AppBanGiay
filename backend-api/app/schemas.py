from pydantic import BaseModel
from typing import List, Optional

# Schema cơ bản cho Sản phẩm
class ProductBase(BaseModel):
    name: str
    price: float
    description: Optional[str] = None
    image: Optional[str] = None
    category_id: int

# Schema dùng để trả dữ liệu về cho Client (App/Web)
class Product(ProductBase):
    id: int
    class Config:
        from_attributes = True 