from fastapi import APIRouter, HTTPException
from services.storage import list_collections
from services.embedding import get_embeddings

router = APIRouter()

embeddings = get_embeddings()

@router.get("", response_model=list[str])
async def collections_endpoint():
    """사용 가능한 컬렉션 목록을 반환"""
    try:
        collections = await list_collections(embeddings=embeddings)
        return collections

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"컬렉션 목록 조회 중 오류 발생: {str(e)}")
