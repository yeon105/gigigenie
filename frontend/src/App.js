import { Box } from "@mui/material";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Route, Routes, useLocation } from "react-router-dom";
import { productList } from "./api/productApi";
import "./App.css";
import ChatPage from "./pages/ChatPage";
import DevicePage from "./pages/DevicePage";
import Header from "./pages/Header";
import LoginPage from "./pages/LoginPage";
import SideLayout from "./pages/SideLayout";
import { setProducts } from "./redux/ProductSlice";

function App() {
  const location = useLocation();
  const dispatch = useDispatch();
  const isLoginPage = location.pathname === "/login";
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const isLoggedIn = useSelector((state) => state.loginSlice.isLogin);
  const sidebarRef = useRef(null);

  // 제품 목록 가져오기
  const fetchProducts = async () => {
    try {
      const data = await productList();
      dispatch(setProducts(data));
    } catch (error) {
      console.error("제품 목록 가져오기 실패:", error);
    }
  };

  // 초기 로드 시 제품 목록 가져오기
  useEffect(() => {
    if (isLoggedIn) {
      fetchProducts();
    }
  }, [isLoggedIn]);

  useEffect(() => {
    function handleClickOutside(event) {
      if (
        sidebarOpen &&
        sidebarRef.current &&
        !sidebarRef.current.contains(event.target) &&
        !event.target.closest(".menu-button")
      ) {
        setSidebarOpen(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [sidebarOpen]);

  useEffect(() => {
    setSidebarOpen(false);
  }, [isLoggedIn]);

  return (
    <div className="App">
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          height: "100vh",
          maxWidth: "480px",
          margin: "0 auto",
          backgroundColor: "#f0f0f0",
          position: "relative",
          overflow: "hidden",
        }}
      >
        {!isLoginPage && (
          <Header
            isLoggedIn={isLoggedIn}
            onMenuClick={() => setSidebarOpen(!sidebarOpen)}
          />
        )}

        <Box
          sx={{
            flex: 1,
            display: "flex",
            flexDirection: "column",
            overflow: "auto",
          }}
        >
          <Routes>
            <Route path="/" element={<DevicePage />} />
            <Route path="/chat" element={<ChatPage />} />
            <Route path="/login" element={<LoginPage />} />
          </Routes>
        </Box>

        {!isLoginPage && isLoggedIn && (
          <Box
            ref={sidebarRef}
            sx={{
              position: "fixed",
              top: "60px",
              width: "250px",
              height: "calc(100% - 60px)",
              transition: "right 0.3s ease",
              zIndex: 1300,
              right: sidebarOpen ? { xs: 0, sm: "calc(50% - 240px)" } : "-250px",
            }}
          >
            <SideLayout 
              onClose={() => setSidebarOpen(false)} 
              onProductUpdate={fetchProducts}
            />
          </Box>
        )}
      </Box>
    </div>
  );
}

export default App;
