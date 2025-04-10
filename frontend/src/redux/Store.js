import { configureStore } from "@reduxjs/toolkit";
import loginSlice from "./LoginSlice";
import productSlice from "./ProductSlice";

const store = configureStore({
  reducer: {
    login: loginSlice,
    product: productSlice,
  },
});

export default store;
