from fastapi import FastAPI
from routes import search
# from routes import upload
# from routes import collections

app = FastAPI()

# 라우터 등록
app.include_router(search.router, prefix="/api", tags=["search"])
# app.include_router(upload.router, prefix="/api/upload", tags=["upload"])
# app.include_router(collections.router, prefix="/api/collections", tags=["collections"])

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
