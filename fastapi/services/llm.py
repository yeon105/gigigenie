# services/llm.py
import os
from dotenv import load_dotenv
import google.generativeai as genai

load_dotenv()

# API 키 확인
api_key = os.getenv("GEMINI_API_KEY")
if not api_key:
    raise ValueError("GEMINI_API_KEY 환경 변수가 설정되지 않았습니다.")

genai.configure(api_key=api_key)

async def create_answer_with_gemini(query: str, retrieved_docs: list[dict]) -> str:
    try:
        context = "\n\n".join([doc["content"] for doc in retrieved_docs])
        
        prompt = f"""
        사용자의 질문: {query}
        
        아래는 검색된 문서들의 내용입니다:
        {context}

        위 내용을 바탕으로 사용자의 질문에 대해 정확도가 높고 사용자가 쉽게 이해할 수 있게 간단히 요약해서 설명해주세요.
        """

        model = genai.GenerativeModel("gemini-2.0-flash")
        response = model.generate_content(prompt)
        return response.text.strip()
        
    except Exception as e:
        raise Exception(f"답변 생성 중 오류 발생: {str(e)}")
