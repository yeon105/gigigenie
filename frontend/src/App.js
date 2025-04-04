import "./App.css";
import { Routes, Route, useLocation } from "react-router-dom";
import Header from "./pages/Header";
import DevicePage from "./pages/DevicePage";
import ChatPage from "./pages/ChatPage";
import SideLayout from "./pages/SideLayout";
import LoginPage from "./pages/LoginPage";
import { Box } from "@mui/material";
import { useState, useRef, useEffect } from "react";
import { useSelector } from "react-redux";

function App() {
  const location = useLocation();
  const isLoginPage = location.pathname === "/login";
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const isLoggedIn = useSelector((state) => state.loginSlice.isLogin);
  const sidebarRef = useRef(null);

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
              position: "absolute",
              top: "60px",
              right: sidebarOpen ? 0 : "-250px",
              width: "250px",
              height: "calc(100% - 60px)",
              transition: "right 0.3s ease",
              zIndex: 1000,
            }}
          >
            <SideLayout onClose={() => setSidebarOpen(false)} />
          </Box>
        )}
      </Box>
    </div>
  );
}

export default App;
