# from fastapi import APIRouter, HTTPException, UploadFile, File, Form
# from services.storage import process_pdf

# router = APIRouter()

# @router.post("/")
# async def upload_endpoint(file: UploadFile = File(...), collection_name: str = Form("langchain")):
#     """PDF 파일을 업로드하고 처리"""
#     try:
#         # 파일 내용 읽기
#         file_content = await file.read()
        
#         # PDF 처리
#         result = await process_pdf(
#             file_content=file_content,
#             file_name=file.filename,
#             collection_name=collection_name
#         )
        
#         return result
        
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=f"파일 처리 중 오류 발생: {str(e)}")
