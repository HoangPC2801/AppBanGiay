from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .database import engine
from . import models
from .routers import products  

models.Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Hệ thống Bán Giày API",
    description="API Backend cho đồ án tốt nghiệp hệ thống bán giày",
    version="1.0.0"
)

# CẤU HÌNH CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Cho phép tất cả các nguồn (hoặc điền URL web admin)
    allow_credentials=True,
    allow_methods=["*"],  # Cho phép tất cả các phương thức GET, POST, PUT, DELETE
    allow_headers=["*"],
)

app.include_router(products.router)

@app.get("/", tags=["Root"])
def read_root():
    return {
        "status": "online",
        "message": "Server FastAPI Hệ thống bán giày đang hoạt động ổn định!",
        "docs": "/docs"
    }