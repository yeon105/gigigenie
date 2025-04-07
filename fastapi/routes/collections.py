# from fastapi import APIRouter, HTTPException
# from services.storage import list_collections

# router = APIRouter()

# @router.get("")
# async def list_collections_endpoint():
#     """컬렉션 목록 조회"""
#     try:
#         collections = await list_collections()
#         return collections
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=f"컬렉션 목록 조회 중 오류 발생: {str(e)}")
