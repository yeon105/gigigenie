import axios from 'axios';

export const productList = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/product/list');
        return response.data;
    } catch (error) {
        console.error("제품 전체조회 실패:", error);
        throw error;
    }
};