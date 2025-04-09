import { configureStore } from "@reduxjs/toolkit";
import loginSlice from "./LoginSlice";
import productSlice from "./ProductSlice";

const store = configureStore({
  reducer: {
    loginSlice: loginSlice,
    productSlice: productSlice,
  },
});

export default store;
