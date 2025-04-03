# storage.py
from langchain_postgres import PGVector
from langchain_community.document_loaders import PyMuPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
import tempfile
import os

# 데이터베이스 연결 정보
connection = "postgresql+psycopg://langchain:langchain@localhost:5432/langchain"

def get_vector_store(collection_name="langchain", embeddings=None):
    """벡터 저장소 인스턴스를 반환합니다."""
    if not embeddings:
        from services.embedding import get_embeddings
        embeddings = get_embeddings()
    
    return PGVector(
        embeddings=embeddings,
        collection_name=collection_name,
        connection=connection,
        use_jsonb=True,
    )

async def process_pdf(file_content, file_name, collection_name="langchain", chunk_size=210, chunk_overlap=50, embeddings=None):
    """PDF 파일을 처리하고 벡터 저장소에 저장합니다."""
    # 임시 파일 생성 및 저장
    with tempfile.NamedTemporaryFile(delete=False, suffix='.pdf') as temp_file:
        temp_file.write(file_content)
        temp_file_path = temp_file.name
    
    try:
        # 문서 로드
        loader = PyMuPDFLoader(temp_file_path)
        docs = loader.load()
        
        # 문서 분할
        text_splitter = RecursiveCharacterTextSplitter(chunk_size=chunk_size, chunk_overlap=chunk_overlap)
        split_documents = text_splitter.split_documents(docs)
        
        # 벡터 저장소 초기화
        vector_store = get_vector_store(collection_name, embeddings)
        
        # 문서 추가
        vector_store.add_documents(split_documents)
        
        return {
            "status": "success",
            "message": f"{file_name} 파일이 성공적으로 처리되었습니다.",
            "chunks": len(split_documents),
            "collection": collection_name
        }
    
    finally:
        # 임시 파일 삭제
        if os.path.exists(temp_file_path):
            os.unlink(temp_file_path)

async def list_collections(embeddings=None):
    """사용 가능한 컬렉션 목록을 반환합니다."""
    # 벡터 저장소 연결
    vector_store = get_vector_store(embeddings=embeddings)
    
    # 컬렉션 목록 가져오기
    with vector_store._make_sync_session() as session:
        # 직접 SQL 쿼리로 컬렉션 이름 조회
        collections_query = session.query(vector_store.CollectionStore.name).distinct()
        collections = [row[0] for row in collections_query]
    
    return collections 