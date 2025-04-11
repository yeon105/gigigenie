import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import SearchIcon from "@mui/icons-material/Search";
import { Box, Typography } from "@mui/material";
import React, { useEffect, useState, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { productList } from "../api/productApi";
import { addFavorite, deleteFavorite, favoriteList } from "../api/favoriteApi";
import { setProducts } from "../redux/ProductSlice";
import { loginSuccess, updateFavorites } from "../redux/LoginSlice";
import "../styles/DevicePage.css";

const DevicePage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [searchQuery, setSearchQuery] = useState("");
  const [isProcessing, setIsProcessing] = useState(false);
  const products = useSelector((state) => state.product.products) || [];
  const userFavorites = useSelector((state) => state.login.favoriteList) || [];
  const userId = useSelector((state) => state.login.user?.id);
  const user = useSelector((state) => state.login.user);

  const fetchProducts = useCallback(async () => {
    try {
      const data = await productList();
      dispatch(setProducts(data));
    } catch (error) {
      console.error("제품 목록 가져오기 실패:", error);
    }
  }, [dispatch]);

  const fetchFavorites = useCallback(async () => {
    if (!userId) return;
    
    try {
      const favorites = await favoriteList(userId);
      if (Array.isArray(favorites)) {
        // Redux 상태 업데이트
        dispatch(updateFavorites(favorites));
        
        // localStorage 업데이트
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
          const userData = JSON.parse(storedUser);
          localStorage.setItem("user", JSON.stringify({
            ...userData,
            favoriteList: favorites
          }));
        }
      }
    } catch (error) {
      console.error("즐겨찾기 목록 가져오기 실패:", error);
      
      // 에러 발생 시 localStorage의 데이터 사용
      const storedUser = localStorage.getItem("user");
      if (storedUser) {
        const userData = JSON.parse(storedUser);
        dispatch(updateFavorites(userData.favoriteList || []));
      }
    }
  }, [userId, dispatch]);

  // 초기 데이터 로드
  useEffect(() => {
    fetchProducts();
    fetchFavorites();
  }, [fetchProducts, fetchFavorites]);

  const filteredDevices = products.filter((device) => {
    if (!device || !device.name) return false;
    return device.name.toLowerCase().includes(searchQuery.toLowerCase());
  });

  const handleDeviceClick = (device) => {
    navigate("/chat", { state: { deviceName: device.name, productId: device.id } });
  };

  const isFavorite = (productId) => {
    return userFavorites.includes(productId);
  };

  const toggleFavorite = async (e, deviceId) => {
    e.stopPropagation();

    if (!userId) {
      alert("로그인이 필요합니다.");
      return;
    }

    if (isProcessing) return;

    setIsProcessing(true);
    try {
      let newFavorites;
      if (isFavorite(deviceId)) {
        await deleteFavorite(userId, deviceId);
        newFavorites = userFavorites.filter(id => id !== deviceId);
      } else {
        await addFavorite(userId, deviceId);
        newFavorites = [...userFavorites, deviceId];
      }
      
      // Redux 상태 업데이트
      dispatch(updateFavorites(newFavorites));
      
      // localStorage 업데이트
      const storedUser = localStorage.getItem("user");
      if (storedUser) {
        const userData = JSON.parse(storedUser);
        localStorage.setItem("user", JSON.stringify({
          ...userData,
          favoriteList: newFavorites
        }));
      }
    } catch (error) {
      console.error("즐겨찾기 토글 실패:", error);
      alert("즐겨찾기 처리 중 오류가 발생했습니다.");
      await fetchFavorites(); // 실패 시 서버에서 최신 데이터 다시 가져오기
    } finally {
      setIsProcessing(false);
    }
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
                style={{ width: "54px", height: "54px" }}
              />
              <div
                className={`favorite-icon ${isFavorite(device.id) ? "active" : ""}`}
                onClick={(e) => toggleFavorite(e, device.id)}
              >
                {isFavorite(device.id) ? (
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