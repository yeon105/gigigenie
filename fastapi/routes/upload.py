from fastapi import APIRouter, UploadFile, File, HTTPException, Query
from services.storage import process_pdf
from services.embedding import get_embeddings

router = APIRouter()

embeddings = get_embeddings()

# 파일 크기 제한 (5MB)
MAX_FILE_SIZE = 5 * 1024 * 1024

@router.post("", response_model=dict)
async def upload_document(
    file: UploadFile = File(...),
    collection_name: str = Query("langchain", description="벡터 저장소 컬렉션 이름"),
    chunk_size: int = Query(210, description="문서 분할 크기"),
    chunk_overlap: int = Query(50, description="문서 분할 오버랩")
):
    """PDF 파일을 업로드하고 벡터 저장소에 저장"""
    if not file.filename.endswith('.pdf'):
        raise HTTPException(status_code=400, detail="PDF 파일만 지원합니다.")

    try:
        contents = await file.read()
        
        # 파일 크기 체크
        if len(contents) > MAX_FILE_SIZE:
            raise HTTPException(
                status_code=413,
                detail=f"파일 크기가 너무 큽니다. 최대 {MAX_FILE_SIZE/1024/1024}MB까지만 업로드 가능합니다."
            )
            
        result = await process_pdf(
            file_content=contents,
            file_name=file.filename,
            collection_name=collection_name,
            chunk_size=chunk_size,
            chunk_overlap=chunk_overlap
        )
        return result

    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 처리 중 오류 발생: {str(e)}")
