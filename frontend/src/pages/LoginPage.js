import { Box, Button, Typography } from "@mui/material";
import React from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import logo from "../images/gigigenie_logo.png";
import google_logo from "../images/Google_Login.png";
import { login } from "../redux/LoginSlice";
import "../styles/LoginPage.css";

const LoginPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleLogin = (provider) => {
    console.log(`Logging in with ${provider}`);
    dispatch(login());
    navigate("/");
  };

  const handleGuestLogin = () => {
    console.log("Logging in as guest");
    dispatch(login());
    navigate("/");
  };

  const handleLogoClick = () => {
    navigate("/");
  };

  return (
    <Box className="login-page-container">
      <Box className="login-wrapper">
        <Box className="login-header-bar">
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <img
              src={logo}
              height="50px"
              alt="Logo"
              onClick={handleLogoClick}
              style={{ cursor: "pointer" }}
            />
          </Box>
        </Box>

        <Box className="login-content">
          <Button
            fullWidth
            variant="outlined"
            className="google-button"
            onClick={() => handleLogin("google")}
          >
            <img
              src={google_logo}
              alt="구글 로고"
              style={{ width: "24px", marginRight: "8px" }}
            />
            구글 계정으로 로그인
          </Button>

          <Box className="divider">
            <Typography variant="body2" color="textSecondary">
              또는
            </Typography>
          </Box>

          <Button
            fullWidth
            variant="contained"
            className="guest-button"
            onClick={handleGuestLogin}
          >
            게스트로 계속하기
          </Button>

          <Typography variant="caption" className="terms-text">
            로그인 시 서비스 이용약관과 개인정보 처리방침에 동의하게 됩니다.
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default LoginPage;
