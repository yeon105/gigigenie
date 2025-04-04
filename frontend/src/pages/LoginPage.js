import { Box, Button, Typography } from "@mui/material";
import React from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { login } from "../redux/LoginSlice";
import ex_logo from "../images/ex_logo.png";
import kakao_logo from "../images/KaKao_Login.png"; // 카카오 로고
import google_logo from "../images/Google_Login.png"; // 구글 로고
import naver_logo from "../images/Naver_Login.png"; // 네이버 로고
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
      <Box className="login-header-bar">
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <img
            src={ex_logo}
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
          variant="contained"
          className="kakao-button"
          onClick={() => handleLogin("kakao")}
        >
          <img
            src={kakao_logo}
            alt="카카오 로고"
            style={{ width: "24px", marginRight: "8px" }}
          />
          카카오 계정으로 로그인
        </Button>

        <Button
          fullWidth
          variant="contained"
          className="naver-button"
          onClick={() => handleLogin("naver")}
        >
          <img
            src={naver_logo}
            alt="네이버 로고"
            style={{ width: "24px", marginRight: "8px" }}
          />
          네이버 계정으로 로그인
        </Button>

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
  );
};

export default LoginPage;
