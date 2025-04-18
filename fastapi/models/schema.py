from pydantic import BaseModel
from typing import List, Optional, Dict, Any

class SearchQuery(BaseModel):
    query: str
    collection_name: str
    top_k: int = 3

class DocumentResponse(BaseModel):
    content: str
    metadata: Dict[str, Any]
    score: Optional[float] = None

class SearchResponse(BaseModel):
    query: str
    documents: List[Dict[str, Any]]
    answer: str