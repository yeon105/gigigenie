import Box from "@mui/material/Box";
import ex_logo from "../images/ex_logo.png";
import { Button, IconButton } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu";

const Header = ({ isLoggedIn, onMenuClick }) => {
  const navigate = useNavigate();

  const handleLoginClick = () => {
    navigate("/login");
  };

  const handleLogoClick = () => {
    navigate("/");
  };

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        padding: "10px 15px",
        justifyContent: "space-between",
        borderBottom: "1px solid #e0e0e0",
        backgroundColor: "#fff",
      }}
    >
      <img
        src={ex_logo}
        height="40px"
        alt="Logo"
        onClick={handleLogoClick}
        style={{ cursor: "pointer" }}
      />

      {isLoggedIn ? (
        <IconButton
          onClick={onMenuClick}
          aria-label="menu"
          className="menu-button"
        >
          <MenuIcon />
        </IconButton>
      ) : (
        <Button
          variant="contained"
          onClick={handleLoginClick}
          size="small"
          sx={{
            backgroundColor: "#f4c542",
            color: "black",
            "&:hover": { backgroundColor: "#e0b73a" },
            fontSize: "12px",
          }}
        >
          로그인
        </Button>
      )}
    </Box>
  );
};

export default Header;
