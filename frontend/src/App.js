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
import { loginSuccess } from "./redux/LoginSlice";

function App() {
  const location = useLocation();
  const dispatch = useDispatch();
  const isLoginPage = location.pathname === "/login";
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { isLogin, user } = useSelector((state) => state.login || {});
  const sidebarRef = useRef(null);
  
  const isUserRole = () => {
    if (!user || !user.role) return false;
    return user.role === "USER";
  };
  
  const getUserRole = () => {
    if (!user || !user.role) return null;
    return user.role;
  };

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    const token = localStorage.getItem("token");
  
    if (storedUser && token) {
      const user = JSON.parse(storedUser);
      dispatch(loginSuccess({
        id: user.id,
        name: user.name,
        role: user.role,
        favoriteList: user.favoriteList || [],
        message: "로그인 유지 중입니다.",
      }));
    }
  }, []);  
  
  const fetchProducts = async () => {
    try {
      const data = await productList();
      dispatch(setProducts(data));
    } catch (error) {
      console.error("제품 목록 가져오기 실패:", error);
    }
  };

  useEffect(() => {
    if (isLogin) fetchProducts();
  }, [isLogin]);

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
  }, [isLogin]);

  const showSidebar = !isLoginPage && isLogin && isUserRole();

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
            isLoggedIn={isLogin}
            userRole={getUserRole()}
            onMenuClick={() => setSidebarOpen(!sidebarOpen)}
          />
        )}

        <Box sx={{ flex: 1, display: "flex", flexDirection: "column", overflow: "auto" }}>
          <Routes>
            <Route path="/" element={<DevicePage />} />
            <Route path="/chat" element={<ChatPage />} />
            <Route path="/login" element={<LoginPage />} />
          </Routes>
        </Box>

        {showSidebar && (
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
            <SideLayout onClose={() => setSidebarOpen(false)} onProductUpdate={fetchProducts} />
          </Box>
        )}
      </Box>
    </div>
  );
}

export default App;