import re
from storage import get_vector_store
from services.embedding import get_llm

async def search_documents(query_text, collection_name="langchain", top_k=3, embeddings=None):
    """벡터 저장소에서 유사 문서를 검색하고, LLM을 사용하여 답변을 생성합니다."""
    
    # 벡터 저장소 초기화
    vector_store = get_vector_store(collection_name, embeddings)
    
    # 유사 문서 검색
    docs_and_scores = vector_store.similarity_search_with_score(query_text, k=top_k)
    
    # 검색된 문서 내용 추출
    context_texts = []
    results = []
    for doc, score in docs_and_scores:
        normalized_content = re.sub(r'\s+', ' ', doc.page_content).strip()
        context_texts.append(normalized_content)
        
        results.append({
            "content": normalized_content,
            "metadata": doc.metadata,
            "score": score
        })

    # 검색된 문서들을 LLM이 참고하여 응답 생성
    context = "\n\n".join(context_texts)
    llm = get_llm()
    
    prompt = f"""
    사용자의 질문: {query_text}
    
    아래는 검색된 문서들의 내용입니다:
    {context}

    위 내용을 바탕으로 사용자의 질문에 대해 정확하고 자세한 답변을 생성하세요.
    """
    
    response = llm.invoke(prompt)  # LLM 호출
    
    return {
        "query": query_text,
        "response": response,
        "documents": results
    }
