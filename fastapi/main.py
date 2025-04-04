from fastapi import FastAPI
from routes import upload, search, collections

app = FastAPI(title="기기지니")

# 라우팅 등록
app.include_router(upload.router, prefix="/api/upload", tags=["Upload"])
app.include_router(search.router, prefix="/api/search", tags=["Search"])
app.include_router(collections.router, prefix="/api/collections", tags=["Collections"])

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
