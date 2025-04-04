import React, { useState } from "react";
import { Box, Typography } from "@mui/material";
import SmartphoneIcon from "@mui/icons-material/Smartphone";
import ComputerIcon from "@mui/icons-material/Computer";
import TvIcon from "@mui/icons-material/Tv";
import KitchenIcon from "@mui/icons-material/Kitchen";
import TabletIcon from "@mui/icons-material/Tablet";
import SearchIcon from "@mui/icons-material/Search";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { useNavigate } from "react-router-dom";
import "../styles/DevicePage.css";

const DevicePage = () => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");
  const [favorites, setFavorites] = useState({});

  const devices = [
    {
      id: "s24",
      name: "Samsung S24",
      icon: <SmartphoneIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "smartphone",
    },
    {
      id: "aaa",
      name: "Samsung AAA",
      icon: <SmartphoneIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "smartphone",
    },
    {
      id: "bbb",
      name: "LG BBB",
      icon: <TvIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "tv",
    },
    {
      id: "iphone16",
      name: "Apple iPhone 16",
      icon: <SmartphoneIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "smartphone",
    },
    {
      id: "ccc",
      name: "LG CCC",
      icon: <KitchenIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "appliance",
    },
    {
      id: "ddd",
      name: "Samsung DDD",
      icon: <ComputerIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "computer",
    },
    {
      id: "eee",
      name: "EEE",
      icon: <KitchenIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "refrigerator",
    },
    {
      id: "fff",
      name: "FFF",
      icon: <KitchenIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "appliance",
    },
    {
      id: "ggg",
      name: "GGG",
      icon: <SmartphoneIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "smartphone",
    },
    {
      id: "hhh",
      name: "HHH",
      icon: <TabletIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "tablet",
    },
    {
      id: "iii",
      name: "III",
      icon: <SmartphoneIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "smartphone",
    },
    {
      id: "jjj",
      name: "JJJ",
      icon: <ComputerIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "computer",
    },
    {
      id: "kkk",
      name: "KKK",
      icon: <TvIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "tv",
    },
    {
      id: "lll",
      name: "LLL",
      icon: <KitchenIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "appliance",
    },
    {
      id: "mmm",
      name: "MMM",
      icon: <TabletIcon className="device-icon" sx={{ fontSize: 54 }} />,
      type: "tablet",
    },
  ];

  const filteredDevices = devices.filter((device) =>
    device.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

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
              {device.icon}
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
