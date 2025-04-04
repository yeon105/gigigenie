import os
import json
import httpx
from typing import List, Dict, Any
from dotenv import load_dotenv
from services.embedding import get_embeddings

load_dotenv()
SPRINGBOOT_API_URL = os.getenv('SPRINGBOOT_API_URL', 'http://localhost:8080')

# ✅ Upstage 임베딩 모델 한 번만 생성 (매번 생성하지 않음)
embeddings = get_embeddings()

async def process_pdf(file_content: bytes, file_name: str, collection_name: str = "langchain", chunk_size: int = 210, chunk_overlap: int = 50) -> Dict[str, Any]:
    """PDF 파일을 Spring Boot API를 통해 처리"""
    try:
        # ✅ 벡터 임베딩 생성
        vector = embeddings.embed_query(file_name)  
        print(f"Generated embedding for {file_name}: {vector[:5]}...")  # 일부 값 출력

        import json  # ✅ JSON 변환을 위한 모듈 추가

        async with httpx.AsyncClient() as client:
            files = {'file': (file_name, file_content, 'application/pdf')}
            data = {
                'collection_name': collection_name,
                'chunk_size': str(chunk_size),
                'chunk_overlap': str(chunk_overlap),
                'embedding': [vector]
            }
            response = await client.post(f"{SPRINGBOOT_API_URL}/api/upload", files=files, data=data)


            
            if response.status_code != 200:
                error_detail = response.text if response.text else "No error details available"
                print(f"SpringBoot API error response: {error_detail}")
                raise Exception(f"SpringBoot API error ({response.status_code}): {error_detail}")
                
            return response.json()
    except httpx.RequestError as e:
        print(f"Connection error: {str(e)}")
        raise Exception(f"SpringBoot API connection error: {str(e)}")
    except Exception as e:
        print(f"Unexpected error: {str(e)}")
        raise Exception(f"SpringBoot API error: {str(e)}")


async def list_collections() -> List[str]:
    """Spring Boot API를 통해 컬렉션 목록을 조회"""
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{SPRINGBOOT_API_URL}/api/collections")  # ✅ 엔드포인트 수정
            
            if response.status_code != 200:
                error_detail = response.text if response.text else "No error details available"
                raise Exception(f"SpringBoot API error ({response.status_code}): {error_detail}")
                
            return response.json()
    except httpx.RequestError as e:
        raise Exception(f"SpringBoot API connection error: {str(e)}")
    except Exception as e:
        raise Exception(f"SpringBoot API error: {str(e)}")


async def search_documents(query_text: str, collection_name: str = "langchain", top_k: int = 3) -> Dict[str, Any]:
    """Spring Boot API를 통해 문서를 검색"""
    try:
        async with httpx.AsyncClient() as client:
            data = {
                'query': query_text,
                'collection_name': collection_name,
                'top_k': top_k
            }
            response = await client.post(f"{SPRINGBOOT_API_URL}/api/search", json=data)  # ✅ 엔드포인트 수정
            
            if response.status_code != 200:
                error_detail = response.text if response.text else "No error details available"
                raise Exception(f"SpringBoot API error ({response.status_code}): {error_detail}")
                
            return response.json()
    except httpx.RequestError as e:
        raise Exception(f"SpringBoot API connection error: {str(e)}")
    except Exception as e:
        raise Exception(f"SpringBoot API error: {str(e)}")
