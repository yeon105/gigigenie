import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import CloseIcon from "@mui/icons-material/Close";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import {
  Box,
  Button,
  IconButton,
  LinearProgress,
  List,
  ListItem,
  ListItemText,
  ListSubheader,
  MenuItem,
  Modal,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { savePdf } from "../api/chatApi";
import { logout } from "../redux/LoginSlice";

const SideLayout = ({ onClose, onProductUpdate }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [openModal, setOpenModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [deviceName, setDeviceName] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const userFavorites = useSelector((state) => state.login.favoriteList) || [];
  const products = useSelector((state) => state.product.products) || [];

  // 즐겨찾기한 제품 목록 필터링
  const favoriteProducts = products.filter(product => 
    userFavorites.includes(product.id)
  );

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    if (!isUploading) {
      setOpenModal(false);
      setSelectedFile(null);
      setDeviceName("");
      setCategoryId("");
    }
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file && file.type === "application/pdf") {
      setSelectedFile(file);
    } else {
      alert("PDF 파일만 업로드 가능합니다.");
    }
  };

  const handleSubmit = async () => {
    if (!selectedFile) {
      alert("PDF 파일을 선택해주세요.");
      return;
    }

    if (!deviceName.trim()) {
      alert("제품명을 입력해주세요.");
      return;
    }

    if (!categoryId) {
      alert("카테고리를 선택해주세요.");
      return;
    }

    setIsUploading(true);
    setUploadProgress(0);
    setOpenModal(false);

    try {
      await savePdf(deviceName, categoryId, selectedFile, (progress) => {
        setUploadProgress(Math.min(progress * 0.9, 90));
      });
      setUploadProgress(100);
      await new Promise(resolve => setTimeout(resolve, 500));
      
      await onProductUpdate();
      
      alert("제품 설명서가 성공적으로 등록되었습니다.");
    } catch (error) {
      alert("제품 설명서 등록에 실패했습니다.");
      console.error("등록 실패:", error);
    } finally {
      setIsUploading(false);
      setUploadProgress(0);
      setSelectedFile(null);
      setDeviceName("");
      setCategoryId("");
    }
  };

  const handleLogout = () => {
    dispatch(logout());
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleDeviceClick = (product) => {
    navigate(`/chat/${product.id}`, { state: { deviceName: product.name } });
    onClose();
  };

  const handleCategoryChange = (event) => {
    setCategoryId(event.target.value);
  };

  return (
    <Box
      sx={{
        width: 300,
        height: "100vh",
        bgcolor: "background.paper",
        position: "fixed",
        left: 0,
        top: 0,
        zIndex: 1200,
        boxShadow: 3,
        display: "flex",
        flexDirection: "column",
      }}
    >
      <Box
        sx={{
          p: 2,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          borderBottom: 1,
          borderColor: "divider",
        }}
      >
        <Typography variant="h6">메뉴</Typography>
        <IconButton onClick={onClose}>
          <CloseIcon />
        </IconButton>
      </Box>

      <List
        sx={{
          width: "100%",
          bgcolor: "background.paper",
          overflow: "auto",
          flex: 1,
        }}
        subheader={
          <ListSubheader component="div" id="nested-list-subheader">
            즐겨찾기
          </ListSubheader>
        }
      >
        {favoriteProducts.map((product) => (
          <ListItem
            key={product.id}
            button
            onClick={() => handleDeviceClick(product)}
          >
            <ListItemText primary={product.name} />
          </ListItem>
        ))}
      </List>

      <Box sx={{ p: 2, borderTop: 1, borderColor: "divider" }}>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddCircleOutlineIcon />}
          onClick={handleOpenModal}
          fullWidth
        >
          제품 설명서 등록
        </Button>
      </Box>

      <Box sx={{ p: 2, borderTop: 1, borderColor: "divider" }}>
        <Button
          variant="outlined"
          color="error"
          onClick={handleLogout}
          fullWidth
        >
          로그아웃
        </Button>
      </Box>

      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="upload-modal-title"
        aria-describedby="upload-modal-description"
      >
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "background.paper",
            boxShadow: 24,
            p: 4,
            borderRadius: 1,
          }}
        >
          <Typography id="upload-modal-title" variant="h6" component="h2">
            제품 설명서 등록
          </Typography>
          <Stack spacing={2} sx={{ mt: 2 }}>
            <TextField
              label="제품명"
              value={deviceName}
              onChange={(e) => setDeviceName(e.target.value)}
              fullWidth
            />
            <TextField
              select
              label="카테고리"
              value={categoryId}
              onChange={handleCategoryChange}
              fullWidth
            >
              <MenuItem value="1">TV</MenuItem>
              <MenuItem value="2">냉장고</MenuItem>
              <MenuItem value="3">세탁기</MenuItem>
              <MenuItem value="4">에어컨</MenuItem>
            </TextField>
            <Button
              variant="outlined"
              component="label"
              startIcon={<UploadFileIcon />}
              fullWidth
            >
              PDF 파일 선택
              <input
                type="file"
                hidden
                accept=".pdf"
                onChange={handleFileChange}
              />
            </Button>
            {selectedFile && (
              <Typography variant="body2" color="text.secondary">
                선택된 파일: {selectedFile.name}
              </Typography>
            )}
            <Button
              variant="contained"
              color="primary"
              onClick={handleSubmit}
              disabled={isUploading}
              fullWidth
            >
              {isUploading ? "업로드 중..." : "등록하기"}
            </Button>
          </Stack>
        </Box>
      </Modal>

      {isUploading && (
        <Box sx={{ width: "100%", position: "fixed", bottom: 0, left: 0 }}>
          <LinearProgress variant="determinate" value={uploadProgress} />
        </Box>
      )}
    </Box>
  );
};

export default SideLayout;
