# query.py
import re
from storage import get_vector_store

async def search_documents(query_text, collection_name="langchain", top_k=3, embeddings=None):
    """벡터 저장소에서 유사 문서를 검색합니다."""
    # 벡터 저장소 초기화
    vector_store = get_vector_store(collection_name, embeddings)
    
    # 유사 문서 검색
    docs_and_scores = vector_store.similarity_search_with_score(
        query_text, 
        k=top_k
    )
    
    # 응답 형식 변환
    results = []
    for doc, score in docs_and_scores:
        normalized_content = re.sub(r'\s+', ' ', doc.page_content).strip()
        
        results.append({
            "content": normalized_content,
            "metadata": doc.metadata,
            "score": score
        })
    
    return results 