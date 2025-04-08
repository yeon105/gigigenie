import axios from 'axios';

export const createAnswer = async (query, collection_name, top_k) => {
    try {
        const body = {
            "query": query,
            "collection_name": collection_name,
            "top_k": top_k
          }

        const response = await axios.post('http://localhost:8080/api/chat/ask', body);
        return response.data;
    } catch (error) {
        console.error("답변 생성 실패:", error);
        throw error;
    }
};