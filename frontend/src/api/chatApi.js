import axiosInstance from './axiosInstance';
import store from '../redux/Store';

export const savePdf = async (name, categoryId, file, imageFile = null) => {
    try {
        const formData = new FormData();
        formData.append('name', name);
        formData.append('categoryId', categoryId);
        formData.append('file', file);
        
        if (imageFile) {
            formData.append('image', imageFile);
        }

        const response = await axiosInstance.post('/pdf/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (error) {
        console.error("PDF 저장 실패:", error);
        throw error;
    }
};

export const createAnswer = async (query, collection_name, top_k) => {
    try {
        const body = {
            "query": query,
            "collection_name": collection_name,
            "top_k": top_k
          }

        const response = await axiosInstance.post('/chat/ask', body);
        return response.data;
    } catch (error) {
        console.error("답변 생성 실패:", error);
        throw error;
    }
};

export const saveChatHistory = async (messages, productId) => {
    try {
        const state = store.getState();
        const memberId = state.login.id;

        const chatHistory = [];
        const filteredMessages = messages.slice(1);
        
        for (let i = 0; i < filteredMessages.length; i += 2) {
            if (i < filteredMessages.length && filteredMessages[i].role === 'user') {
                const queryText = filteredMessages[i].text;
                let responseText = "";
                let queryTime = 0;
                
                if (i + 1 < filteredMessages.length && filteredMessages[i + 1].role === 'bot') {
                    responseText = filteredMessages[i + 1].text;
                    queryTime = filteredMessages[i + 1].queryTime || 0;
                }
                
                chatHistory.push({
                    queryText: queryText,
                    responseText: responseText,
                    queryTime: queryTime
                });
            }
        }

        const response = await axiosInstance.post('/chat/history/save', {
            memberId: memberId,
            productId: productId,
            history: chatHistory
        });
        return response.data;
    } catch (error) {
        console.error("채팅 내역 저장 실패:", error);
        throw error;
    }
};
