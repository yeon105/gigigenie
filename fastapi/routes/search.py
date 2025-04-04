from fastapi import APIRouter, HTTPException
from services.query import search_documents
from models.schema import SearchQuery
from services.embedding import get_embeddings

router = APIRouter()

embeddings = get_embeddings()

@router.post("", response_model=dict)
async def search_endpoint(query: SearchQuery):
    """벡터 저장소에서 유사 문서를 검색"""
    try:
        results = await search_documents(
            query_text=query.query,
            collection_name=query.collection_name,
            top_k=query.top_k,
            embeddings=embeddings
        )
        return results

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"검색 중 오류 발생: {str(e)}")
