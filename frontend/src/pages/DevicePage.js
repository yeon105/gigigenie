import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import SearchIcon from "@mui/icons-material/Search";
import { Box, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { productList } from "../api/productApi";
import { setProducts } from "../redux/ProductSlice";
import "../styles/DevicePage.css";

const DevicePage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [searchQuery, setSearchQuery] = useState("");
  const [favorites, setFavorites] = useState({});
  
  const products = useSelector((state) => state.productSlice.products) || [];

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await productList();
        dispatch(setProducts(data));
      } catch (error) {
        console.error("제품 목록 가져오기 실패:", error);
      }
    };
    fetchProducts();
  }, [dispatch]);

  const filteredDevices = products.filter((device) => {
    if (!device || !device.name) return false;
    return device.name.toLowerCase().includes(searchQuery.toLowerCase());
  });

  const handleDeviceClick = (device) => {
    navigate("/chat", { state: { deviceName: device.name } });
  };

  const toggleFavorite = (e, deviceId) => {
    e.stopPropagation();
    setFavorites((prev) => ({
      ...prev,
      [deviceId]: !prev[deviceId],
    }));
  };

  return (
    <Box className="device-page-container">
      <Box className="search-bar">
        <SearchIcon sx={{ color: "#777777" }} />
        <input
          type="text"
          placeholder="Search..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </Box>

      <Box className="device-grid">
        {filteredDevices.map((device) => (
          <Box
            key={device.id}
            className="device-item"
            onClick={() => handleDeviceClick(device)}
          >
            <Box className="device-icon-container">
              <img 
                src={device.icon} 
                alt={device.name}
                style={{ width: '54px', height: '54px' }}
              />
              <div
                className={`favorite-icon ${
                  favorites[device.id] ? "active" : ""
                }`}
                onClick={(e) => toggleFavorite(e, device.id)}
              >
                {favorites[device.id] ? (
                  <FavoriteIcon fontSize="small" />
                ) : (
                  <FavoriteBorderIcon fontSize="small" />
                )}
              </div>
            </Box>
            <Typography className="device-name">{device.name}</Typography>
          </Box>
        ))}
      </Box>
    </Box>
  );
};

export default DevicePage;
