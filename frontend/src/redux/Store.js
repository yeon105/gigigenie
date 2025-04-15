import { configureStore } from "@reduxjs/toolkit";
import loginReducer from "./LoginSlice";
import productReducer from "./ProductSlice";

const store = configureStore({
  reducer: {
    login: loginReducer,
    product: productReducer,
  },
});

export default store;
