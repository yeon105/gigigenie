import { Box, Button, TextField, Typography, Link, Alert } from "@mui/material";
import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import logo from "../images/gigigenie_logo.png";
import { loginStart, loginSuccess, loginFailure, clearError } from "../redux/LoginSlice";
import { login as loginApi, signup, checkEmailDuplicate } from "../api/authApi";
import "../styles/LoginPage.css";

const LoginPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { loading, error: reduxError, message } = useSelector((state) => state.login || {});
  const [isLogin, setIsLogin] = useState(true);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [emailError, setEmailError] = useState("");

  useEffect(() => {
    dispatch(clearError());
  }, [dispatch]);

  const validateEmail = (email) => {
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    return emailRegex.test(email);
  };

  const validatePassword = (password) => {
    if (password.length < 8) {
      dispatch(loginFailure("비밀번호는 최소 8자 이상이어야 합니다."));
      return false;
    }
    
    if (!/[0-9]/.test(password)) {
      dispatch(loginFailure("비밀번호는 숫자를 포함해야 합니다."));
      return false;
    }
    
    if (!/[a-z]/.test(password)) {
      dispatch(loginFailure("비밀번호는 소문자를 포함해야 합니다."));
      return false;
    }
    
    if (!/[A-Z]/.test(password)) {
      dispatch(loginFailure("비밀번호는 대문자를 포함해야 합니다."));
      return false;
    }
    
    if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
      dispatch(loginFailure("비밀번호는 특수문자를 포함해야 합니다."));
      return false;
    }
    
    return true;
  };

  const handleEmailChange = async (e) => {
    const value = e.target.value;
    setEmail(value);
    setEmailError("");

    if (!validateEmail(value)) {
      setEmailError("올바른 이메일 형식이 아닙니다.");
      return;
    }

    if (!isLogin && value) {
      try {
        const { isDuplicate } = await checkEmailDuplicate(value);
        if (isDuplicate) {
          setEmailError("이미 사용 중인 이메일입니다.");
        }
      } catch (error) {
        setEmailError("이메일 중복 확인 중 오류가 발생했습니다.");
      }
    }
  };

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
  };

  const handleLogin = async () => {
    if (!validateEmail(email)) {
      dispatch(loginFailure("올바른 이메일 형식이 아닙니다."));
      return;
    }

    try {
      dispatch(loginStart());
      const response = await loginApi(email, password);
      localStorage.setItem('token', response.token);
      dispatch(loginSuccess({ 
        member: response.member,
        message: "로그인에 성공했습니다." 
      }));
      navigate("/");
    } catch (error) {
      dispatch(loginFailure(error.message || "로그인에 실패했습니다."));
    }
  };

  const handleSignup = async () => {
    if (!validateEmail(email)) {
      dispatch(loginFailure("올바른 이메일 형식이 아닙니다."));
      return;
    }

    if (!validatePassword(password)) {
      dispatch(loginFailure("비밀번호는 최소 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."));
      return;
    }

    if (password !== confirmPassword) {
      dispatch(loginFailure("비밀번호가 일치하지 않습니다."));
      return;
    }

    if (emailError) {
      dispatch(loginFailure("이메일 중복을 확인해주세요."));
      return;
    }

    try {
      dispatch(loginStart());
      const response = await signup(name, email, password);
      setIsLogin(true);
      dispatch(loginSuccess({ message: "회원가입이 완료되었습니다. 로그인해주세요." }));
      setName("");
      setEmail("");
      setPassword("");
      setConfirmPassword("");
    } catch (error) {
      dispatch(loginFailure(error.message || "회원가입에 실패했습니다."));
    }
  };

  const handleGuestLogin = () => {
    dispatch(loginSuccess({ role: 'GUEST' }));
    navigate("/");
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isLogin) {
      handleLogin();
    } else {
      handleSignup();
    }
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
          <Typography variant="h6" sx={{ mb: 2, textAlign: "center" }}>
            {isLogin ? "로그인" : "회원가입"}
          </Typography>

          {(reduxError || emailError) && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {reduxError || emailError}
            </Alert>
          )}

          {message && (
            <Alert severity="success" sx={{ mb: 2 }}>
              {message}
            </Alert>
          )}

          <form onSubmit={handleSubmit}>
            {!isLogin && (
              <TextField
                fullWidth
                label="이름"
                variant="outlined"
                value={name}
                onChange={(e) => setName(e.target.value)}
                sx={{ mb: 2 }}
                required
              />
            )}

            <TextField
              fullWidth
              label="이메일"
              variant="outlined"
              type="email"
              value={email}
              onChange={handleEmailChange}
              error={!!emailError}
              helperText={emailError}
              sx={{ mb: 2 }}
              required
            />

            <TextField
              fullWidth
              label="비밀번호"
              variant="outlined"
              type="password"
              value={password}
              onChange={handlePasswordChange}
              sx={{ mb: 2 }}
              required
            />

            {!isLogin && (
              <TextField
                fullWidth
                label="비밀번호 확인"
                variant="outlined"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                sx={{ mb: 2 }}
                required
              />
            )}

            <Button
              fullWidth
              variant="contained"
              type="submit"
              className="login-button"
              disabled={loading || (!isLogin && !!emailError)}
              sx={{
                backgroundColor: "#f4c542",
                color: "black",
                "&:hover": { backgroundColor: "#e0b73a" },
                mb: 2,
              }}
            >
              {loading ? "처리 중..." : (isLogin ? "로그인" : "회원가입")}
            </Button>
          </form>

          <Box sx={{ textAlign: "center", mb: 2 }}>
            <Typography variant="body2">
              {isLogin ? "계정이 없으신가요? " : "이미 계정이 있으신가요? "}
              <Link
                component="button"
                variant="body2"
                onClick={() => {
                  setIsLogin(!isLogin);
                  dispatch(clearError());
                  setEmailError("");
                }}
                sx={{ color: "#f4c542" }}
              >
                {isLogin ? "회원가입" : "로그인"}
              </Link>
            </Typography>
          </Box>

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
            sx={{ mt: 2 }}
          >
            게스트로 계속하기
          </Button>

          <Typography variant="caption" className="terms-text">
            {isLogin
              ? "로그인 시 서비스 이용약관과 개인정보 처리방침에 동의하게 됩니다."
              : "회원가입 시 서비스 이용약관과 개인정보 처리방침에 동의하게 됩니다."}
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default LoginPage;
