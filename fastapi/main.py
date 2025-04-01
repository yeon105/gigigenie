# main.py
from fastapi import FastAPI, UploadFile, File, HTTPException, Query
from fastapi.responses import JSONResponse
from typing import List
import uvicorn

from models import SearchQuery, DocumentResponse
from embedding import get_embeddings
from storage import process_pdf, list_collections
from query import search_documents

app = FastAPI(title="기기지니")

# 임베딩 모델 초기화
embeddings = get_embeddings()

@app.post("/upload", response_model=dict)
async def upload_document(
    file: UploadFile = File(...),
    collection_name: str = Query("langchain", description="벡터 저장소 컬렉션 이름"),
    chunk_size: int = Query(210, description="문서 분할 크기"),
    chunk_overlap: int = Query(50, description="문서 분할 오버랩")
):
    """PDF 파일을 업로드하고 벡터 저장소에 저장합니다."""
    
    # 파일 확장자 확인
    if not file.filename.endswith('.pdf'):
        raise HTTPException(status_code=400, detail="PDF 파일만 지원합니다.")
    
    try:
        # 파일 내용 읽기
        contents = await file.read()
        
        # 문서 처리 및 저장
        result = await process_pdf(
            file_content=contents,
            file_name=file.filename,
            collection_name=collection_name,
            chunk_size=chunk_size,
            chunk_overlap=chunk_overlap,
            embeddings=embeddings
        )
        
        return result
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 처리 중 오류 발생: {str(e)}")

@app.post("/search", response_model=dict)
async def search_endpoint(query: SearchQuery):
    """벡터 저장소에서 유사 문서를 검색합니다."""
    try:
        results = await search_documents(
            query_text=query.query,
            collection_name=query.collection_name,
            top_k=query.top_k,
            embeddings=embeddings
        )
        
        return JSONResponse(content=results)
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"검색 중 오류 발생: {str(e)}")

@app.get("/collections", response_model=List[str])
async def collections_endpoint():
    """사용 가능한 컬렉션 목록을 반환합니다."""
    try:
        collections = await list_collections(embeddings=embeddings)
        return collections
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"컬렉션 목록 조회 중 오류 발생: {str(e)}")

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
