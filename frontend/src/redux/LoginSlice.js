import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLogin: false,
  loading: false,
  error: null,
  message: null,
  user: null,
  accessToken: null,
  favoriteList: [],
};

const loginSlice = createSlice({
  name: "login",
  initialState,
  reducers: {
    loginStart: (state) => {
      state.loading = true;
      state.error = null;
    },
    loginSuccess: (state, action) => {
      state.isLogin = true;
      state.loading = false;
      state.error = null;
      state.user = {
        id: action.payload.id,
        name: action.payload.name,
        role: action.payload.role,
      };
      state.accessToken = action.payload.accessToken;
      state.message = action.payload.message;
      state.favoriteList = action.payload.favoriteList || [];
    },
    loginFailure: (state, action) => {
      state.loading = false;
      state.error = action.payload;
    },
    logout: (state) => {
      state.isLogin = false;
      state.user = null;
      state.accessToken = null;
      state.favoriteList = [];
    },
    clearError: (state) => {
      state.error = null;
      state.message = null;
    },
    updateFavorites: (state, action) => {
      state.favoriteList = action.payload;
    },
    updateAccessToken: (state, action) => {
      state.accessToken = action.payload;
    }
  },
});

export const { 
  loginStart, 
  loginSuccess, 
  loginFailure, 
  logout, 
  clearError, 
  updateFavorites,
  updateAccessToken 
} = loginSlice.actions;

export default loginSlice.reducer;
