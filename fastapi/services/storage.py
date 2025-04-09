import os
import json
import httpx
from typing import List, Dict, Any
from dotenv import load_dotenv
from services.embedding import get_embeddings, get_llm
from langchain_postgres import PGVector
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker
# from langchain_community.document_loaders import PyMuPDFLoader
# from langchain_text_splitters import RecursiveCharacterTextSplitter
# import tempfile
from services.llm import create_answer_with_gemini
import re

load_dotenv()

embeddings = get_embeddings()

async def get_vector_store(collection_name="embedding", embeddings=None):
    """벡터 저장소 인스턴스를 반환합니다."""
    try:
        if not embeddings:
            embeddings = get_embeddings()
        
        POSTGRES_USER = os.getenv("POSTGRES_USER")
        POSTGRES_PASSWORD = os.getenv("POSTGRES_PASSWORD")
        POSTGRES_DB = os.getenv("POSTGRES_DB")
        POSTGRES_HOST = os.getenv("POSTGRES_HOST")
        POSTGRES_PORT = os.getenv("POSTGRES_PORT")
        
        print(f"DB 연결 정보: host={POSTGRES_HOST}, port={POSTGRES_PORT}, db={POSTGRES_DB}")
        
        if not all([POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB, POSTGRES_HOST, POSTGRES_PORT]):
            raise ValueError("필수 데이터베이스 환경 변수가 설정되지 않았습니다.")
        
        connection_string = f"postgresql+asyncpg://{POSTGRES_USER}:{POSTGRES_PASSWORD}@{POSTGRES_HOST}:{POSTGRES_PORT}/{POSTGRES_DB}"
        engine = create_async_engine(connection_string)
        
        async_session = sessionmaker(engine, class_=AsyncSession, expire_on_commit=False)
        
        vector_store = PGVector(
            embedding_function=embeddings,
            collection_name="embedding",
            collection_metadata={
                "embedding_column": "embedding",
                "document_column": "document",
                "metadata_column": "cmetadata",
                "id_column": "id",
                "distance_strategy": "cosine"
            },
            engine=engine,
            async_session=async_session
        )
        print("PGVector 인스턴스 생성 완료")
        return vector_store
        
    except Exception as e:
        print(f"벡터 저장소 초기화 중 오류 발생: {str(e)}")
        raise Exception(f"벡터 저장소 초기화 중 오류 발생: {str(e)}")

# async def process_pdf(file_content: bytes, file_name: str, collection_name: str = "langchain", chunk_size: int = 210, chunk_overlap: int = 50) -> Dict[str, Any]:
#     """PDF 파일을 Spring Boot API를 통해 처리"""
#     try:
#         # ✅ 벡터 임베딩 생성
#         vector = embeddings.embed_query(file_name)  

#         import json

#         async with httpx.AsyncClient() as client:
#             files = {'file': (file_name, file_content, 'application/pdf')}
#             data = {
#                 'collection_name': collection_name,
#                 'chunk_size': str(chunk_size),
#                 'chunk_overlap': str(chunk_overlap),
#                 'embedding': [vector]
#             }
#             response = await client.post(f"{SPRINGBOOT_API_URL}/api/upload", files=files, data=data)

#             if response.status_code != 200:
#                 error_detail = response.text if response.text else "No error details available"
#                 raise Exception(f"SpringBoot API error ({response.status_code}): {error_detail}")
                
#             return response.json()
#     except httpx.RequestError as e:
#         raise Exception(f"SpringBoot API connection error: {str(e)}")
#     except Exception as e:
#         raise Exception(f"SpringBoot API error: {str(e)}")

# async def list_collections() -> List[str]:
#     """Spring Boot API를 통해 컬렉션 목록을 조회"""
#     try:
#         async with httpx.AsyncClient() as client:
#             response = await client.get(f"{SPRINGBOOT_API_URL}/api/collections")
            
#             if response.status_code != 200:
#                 error_detail = response.text if response.text else "No error details available"
#                 raise Exception(f"SpringBoot API error ({response.status_code}): {error_detail}")
                
#             return response.json()
#     except httpx.RequestError as e:
#         raise Exception(f"SpringBoot API connection error: {str(e)}")
#     except Exception as e:
#         raise Exception(f"SpringBoot API error: {str(e)}")

async def search_documents(query_text: str, collection_name: str = "embedding", top_k: int = 3, embeddings=None) -> Dict[str, Any]:
    """벡터 저장소에서 유사 문서를 검색하고 답변을 생성합니다."""
    try:
        print(f"검색 시작: query={query_text}, collection={collection_name}, top_k={top_k}")
        vector_store = await get_vector_store(collection_name, embeddings)
        print("벡터 저장소 연결 성공")
        
        docs_and_scores = vector_store.similarity_search_with_score(query_text, k=top_k)
        print(f"검색 결과 개수: {len(docs_and_scores)}")
        
        results = []
        for doc, score in docs_and_scores:
            normalized_content = re.sub(r'\s+', ' ', doc.page_content).strip()
            results.append({
                "content": normalized_content,
                "metadata": doc.metadata,
                "score": score
            })

        print("Gemini 답변 생성 시작")
        answer = await create_answer_with_gemini(query_text, results)
        print("Gemini 답변 생성 완료")
        
        return {
            "query": query_text,
            "answer": answer,
            "documents": results
        }
        
    except Exception as e:
        print(f"문서 검색 중 오류 발생: {str(e)}")
        raise Exception(f"문서 검색 중 오류 발생: {str(e)}")
