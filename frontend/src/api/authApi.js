import axios from 'axios';

const API_URL = 'http://localhost:8080/api'; // Spring 백엔드 서버 주소

export const login = async (email, password) => {
  try {
    const response = await axios.post(`${API_URL}/auth/login`, {
      email,
      password
    });
    console.log("Login successful. Token:", response.data.token);
    console.log("User info:", response.data.member);
    return response.data;
  } catch (error) {
    console.error("Login error:", error.response?.data || error.message);
    throw error;
  }
};

export const signup = async (name, email, password) => {
  try {
    const response = await axios.post(`${API_URL}/auth/signup`, {
      name,
      email,
      password
    });
    console.log("Signup successful. Token:", response.data.token);
    console.log("User info:", response.data.member);
    return response.data;
  } catch (error) {
    console.error("Signup error:", error.response?.data || error.message);
    throw error;
  }
};

export const checkEmailDuplicate = async (email) => {
  try {
    const response = await axios.get(`${API_URL}/auth/check-email?email=${email}`);
    return response.data;
  } catch (error) {
    console.error("Email check error:", error.response?.data || error.message);
    throw error;
  }
}; 