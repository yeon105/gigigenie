import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLogin: false,
  user: null,
  loading: false,
  error: null,
  message: null
};

const loginSlice = createSlice({
  name: "loginSlice",
  initialState,
  reducers: {
    loginStart: (state) => {
      state.loading = true;
      state.error = null;
      state.message = null;
    },
    loginSuccess: (state, action) => {
      state.isLogin = true;
      state.user = action.payload.member;
      state.loading = false;
      state.error = null;
      state.message = action.payload.message;
    },
    loginFailure: (state, action) => {
      state.loading = false;
      state.error = action.payload;
      state.message = null;
    },
    logout: (state) => {
      state.isLogin = false;
      state.user = null;
      state.message = null;
    },
    clearError: (state) => {
      state.error = null;
      state.message = null;
    }
  },
});

export const { loginStart, loginSuccess, loginFailure, logout, clearError } = loginSlice.actions;
export default loginSlice.reducer;
