# embedding.py
import os
from langchain_upstage import UpstageEmbeddings
from dotenv import load_dotenv

# 환경 변수 로드
load_dotenv()
api_key = os.getenv('UPSTAGE_API_KEY')

def get_embeddings():
    """임베딩 모델을 초기화합니다."""
    return UpstageEmbeddings(model="solar-embedding-1-large") 