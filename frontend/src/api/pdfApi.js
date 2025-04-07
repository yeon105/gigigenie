import axios from 'axios';

export const savePdf = async (name, category, file, onProgress) => {
    try {
        const formData = new FormData();
        formData.append('name', name);
        formData.append('category', category);
        formData.append('file', file);

        const response = await axios.post('http://localhost:8080/api/pdf/upload', formData, {
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