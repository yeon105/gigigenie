import os
from langchain_upstage import UpstageEmbeddings
from dotenv import load_dotenv

load_dotenv()
api_key = os.getenv('UPSTAGE_API_KEY')

def get_embeddings():
    """임베딩 모델을 초기화"""
    return UpstageEmbeddings(model="solar-embedding-1-large")
