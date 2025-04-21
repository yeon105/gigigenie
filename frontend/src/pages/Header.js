import Box from "@mui/material/Box";
import logo from "../images/manulo_logo.png";
import { Button, IconButton, Badge } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu";
import LogoutIcon from "@mui/icons-material/Logout";
import NotificationsIcon from "@mui/icons-material/Notifications";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../redux/LoginSlice";
import { markAllAsRead, removeNotification } from "../redux/NotificationSlice";
import { logoutPost } from "../api/loginApi";
import "../styles/Header.css";
import { useState } from "react";
import NotificationComponent from "../components/NotificationComponent";

const Header = ({ isLoggedIn, onMenuClick, userRole }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [anchorEl, setAnchorEl] = useState(null);
  const notifications = useSelector((state) => state.notification.notifications);
  const unreadCount = notifications.filter(notif => !notif.read).length;

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

  const handleNotificationClick = (event) => {
    setAnchorEl(event.currentTarget);
    if (unreadCount > 0) {
      dispatch(markAllAsRead());
    }
  };

  const handleCloseNotifications = () => {
    setAnchorEl(null);
  };

  const handleClearNotification = (id) => {
    dispatch(removeNotification(id));
  };

  const showMenuButton = isLoggedIn && userRole === "USER";
  const notificationOpen = Boolean(anchorEl);

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
              <Badge 
                badgeContent={unreadCount} 
                color="error"
                className={unreadCount > 0 ? "badge-animation" : ""}
              >
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

      <NotificationComponent
        anchorEl={anchorEl}
        open={notificationOpen}
        onClose={handleCloseNotifications}
        notifications={notifications}
        onClearNotification={handleClearNotification}
      />
    </Box>
  );
};

export default Header;
