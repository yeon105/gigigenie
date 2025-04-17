import Box from "@mui/material/Box";
import logo from "../images/gigigenie_logo.png";
import { Button, IconButton } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu";
import LogoutIcon from "@mui/icons-material/Logout";
import { useDispatch } from "react-redux";
import { logout } from "../redux/LoginSlice";
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

  const handleLogout = () => {
    dispatch(logout());
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/");
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
