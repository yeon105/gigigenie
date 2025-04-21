import axiosInstance from './axiosInstance';
import store from '../redux/Store';

export const savePdf = async (name, categoryId, file, onProgress) => {
    try {
        const formData = new FormData();
        formData.append('name', name);
        formData.append('categoryId', categoryId);
        formData.append('file', file);

        const response = await axiosInstance.post('/pdf/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            onUploadProgress: (progressEvent) => {
                const percentCompleted = Math.round(
                    (progressEvent.loaded * 100) / progressEvent.total
                );
                onProgress(percentCompleted);
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

        // 첫 번째 메시지(인사말)를 제외한 나머지 메시지들을 변환
        const chatHistory = messages.slice(1).map(msg => ({
            queryText: msg.role === 'user' ? msg.text : null,
            responseText: msg.role === 'bot' ? msg.text : null,
            queryTime: msg.role === 'bot' ? msg.queryTime : null
        }));

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