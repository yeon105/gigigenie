# embedding.py
import os
from langchain_upstage import UpstageEmbeddings
from langchain_google_genai import GoogleGenerativeAI
from dotenv import load_dotenv

# 환경 변수 로드
load_dotenv()
api_key = os.getenv('UPSTAGE_API_KEY')
gemini_api_key = os.getenv('GEMINI_API_KEY')


def get_embeddings():
    """임베딩 모델을 초기화합니다."""
    return UpstageEmbeddings(model="solar-embedding-1-large") 

def get_llm():
    """GEMINI 모델을 초기화합니다."""
    return GoogleGenerativeAI(model="gemini-2.0-flash", api_key=gemini_api_key)
