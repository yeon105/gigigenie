import Box from "@mui/material/Box";
import logo from "../images/manulo_logo.png";
import { Button, IconButton, Badge } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu";
import LogoutIcon from "@mui/icons-material/Logout";
import NotificationsIcon from "@mui/icons-material/Notifications";
import { useDispatch } from "react-redux";
import { logout } from "../redux/LoginSlice";
import { logoutPost } from "../api/loginApi";
import "../styles/Header.css";

const Header = ({ isLoggedIn, onMenuClick, userRole }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleLoginClick = () => {
    navigate("/login");
  };

  const handleLogoClick = () => {
    navigate("/");
  };

  const handleLogout = async () => {
    try {
      await logoutPost();
      dispatch(logout());
      navigate("/");
    } catch (error) {
      console.error("로그아웃 실패:", error);
      dispatch(logout());
      navigate("/");
    }
  };

  const handleNotificationClick = () => {
    // TODO: 알림 목록 표시 로직 구현
    console.log("알림 버튼 클릭");
  };

  const showMenuButton = isLoggedIn && userRole === "USER";

  return (
    <Box className="header">
      <img
        src={logo}
        alt="Logo"
        onClick={handleLogoClick}
        className="header-logo"
      />
      <Box sx={{ display: 'flex', gap: 1 }}>
        {showMenuButton && (
          <>
            <IconButton
              onClick={handleNotificationClick}
              aria-label="notifications"
              className="notification-button"
            >
              <Badge badgeContent={0} color="error">
                <NotificationsIcon />
              </Badge>
            </IconButton>
            <IconButton
              onClick={handleLogout}
              aria-label="logout"
              className="logout-button"
            >
              <LogoutIcon />
            </IconButton>
            <IconButton
              onClick={onMenuClick}
              aria-label="menu"
              className="menu-button"
            >
              <MenuIcon />
            </IconButton>
          </>
        )}
        {!isLoggedIn && (
          <Button
            variant="contained"
            onClick={handleLoginClick}
            size="small"
            className="login-btn"
          >
            로그인
          </Button>
        )}
      </Box>
    </Box>
  );
};

export default Header;
