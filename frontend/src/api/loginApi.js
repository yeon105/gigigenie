import axios from "axios";
import axiosInstance from "./axiosInstance";

import { API_SERVER_HOST } from "../config/ApiConfig";

const host = `${API_SERVER_HOST}/api/member`;

// 로그인
export const loginPost = async (id, password) => {
    const response = await axios.post(
        `${host}/login`,
        { id, password },
        {
            withCredentials: true,
        }
    );
    return response.data;
};

// 로그아웃
export const logoutPost = async () => {
    const response = await axiosInstance.post(`/logout`);
    return response.data;
};

// 회원가입
export const joinPost = async (requestData) => {
    const response = await axios.post(`${host}/join`, {
      email: requestData.email,
      password: requestData.password,
      name: requestData.name,
    });
    return response.data;
};

export const checkEmailDuplicate = async (email) => {
  try {
    const response = await axios.get(`${host}/check-email?email=${email}`);
    return response.data;
  } catch (error) {
    console.error("Email check error:", error.response?.data || error.message);
    throw error;
  }
}; 