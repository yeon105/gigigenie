import re
from services.storage import search_documents as springboot_search

async def search_documents(query_text, collection_name="langchain", top_k=3, embeddings=None):
    """SpringBoot API를 통해 문서를 검색하고 결과를 처리"""
    try:
        results = await springboot_search(
            query_text=query_text,
            collection_name=collection_name,
            top_k=top_k
        )
        return results
    except Exception as e:
        raise Exception(f"문서 검색 중 오류 발생: {str(e)}")
