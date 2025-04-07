import os
from langchain_upstage import UpstageEmbeddings, ChatUpstage
from dotenv import load_dotenv

load_dotenv()
api_key = os.getenv('UPSTAGE_API_KEY')

def get_embeddings():
    """임베딩 모델을 초기화"""
    return UpstageEmbeddings(model="solar-embedding-1-large")

def get_llm():
    """LLM 모델을 초기화"""
    return ChatUpstage(
        model="solar-1-mini-chat",
        api_key=api_key
    )

