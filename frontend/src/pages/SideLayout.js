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
      const response = await savePdf(deviceName, categoryId, selectedFile, (progress) => {
        setUploadProgress(Math.min(progress * 0.9, 90));
      });
      
      if (response.status === "exists") {
        alert(`${response.model_name}은(는) 이미 등록된 모델입니다.`);
      } else {
        setUploadProgress(100);
        await new Promise(resolve => setTimeout(resolve, 500));
        await onProductUpdate();
        alert("제품 설명서가 성공적으로 등록되었습니다.");
      }
    } catch (error) {
      alert("제품 설명서 등록에 실패했습니다.");
      console.error("등록 실패:", error);
    } finally {
      setIsUploading(false);
      setUploadProgress(0);
      handleCloseModal();
    }
  };

  const handleLogout = () => {
    dispatch(logout());
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/");
  };

  const handleDeviceClick = (device) => {
    navigate("/chat", { state: { deviceName: device.name, productId: device.id } });
    onClose();
  };

  const handleCategoryChange = (event) => {
    setCategoryId(event.target.value);
  };

  return (
    <Box
      sx={{
        width: "100%",
        height: "100%",
        backgroundColor: "#e0e0e0",
        display: "flex",
        flexDirection: "column",
        boxShadow: "-2px 0 5px rgba(0,0,0,0.1)",
        position: "relative",
      }}
    >
      <IconButton
        onClick={onClose}
        sx={{
          position: "absolute",
          top: 10,
          right: 10,
        }}
      >
        <CloseIcon />
      </IconButton>

      <Box sx={{ padding: "20px 15px 10px" }}>
        <Typography
          variant="subtitle1"
          sx={{ fontWeight: "bold", marginBottom: "10px" }}
        >
          즐겨찾기 (내 디바이스)
        </Typography>

        <List sx={{ width: "100%", padding: 0 }}>
          {favoriteProducts.map((product) => (
            <ListItem
              key={product.id}
              sx={{ padding: "4px 0", cursor: "pointer" }}
              onClick={() => handleDeviceClick(product)}
            >
              <ListItemText primary={product.name} />
            </ListItem>
          ))}
        </List>
      </Box>

      <Box sx={{ marginTop: "auto", padding: "15px" }}>
        <Box sx={{ marginBottom: "15px" }}>
          <Typography
            variant="body2"
            sx={{ marginBottom: "5px", fontSize: "12px" }}
          >
            등록을 원하는 제품의 PDF 설명서를 등록하세요!
          </Typography>
          <Button
            fullWidth
            variant="contained"
            startIcon={<AddCircleOutlineIcon />}
            onClick={handleOpenModal}
            sx={{
              backgroundColor: "#f4c542",
              color: "black",
              "&:hover": { backgroundColor: "#e0b73a" },
              fontSize: "12px",
            }}
          >
            등록
          </Button>
        </Box>

        <Button
          fullWidth
          variant="outlined"
          onClick={handleLogout}
          sx={{ fontSize: "12px" }}
        >
          로그아웃
        </Button>
      </Box>

      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="pdf-upload-modal"
        container={document.body}
      >
        <Paper
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: "85%",
            maxWidth: "350px",
            backgroundColor: "background.paper",
            boxShadow: 24,
            p: 3,
            borderRadius: 2,
          }}
        >
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 2,
            }}
          >
            <Typography variant="h6" component="h2">
              PDF 설명서 등록
            </Typography>
            <IconButton onClick={handleCloseModal} size="small">
              <CloseIcon />
            </IconButton>
          </Box>

          <TextField
            fullWidth
            label="제품명"
            variant="outlined"
            value={deviceName}
            onChange={(e) => setDeviceName(e.target.value)}
            sx={{ mb: 3 }}
            size="small"
          />

          <Box
            sx={{
              border: "2px dashed #ccc",
              borderRadius: 2,
              p: 3,
              textAlign: "center",
              mb: 3,
              backgroundColor: "#f9f9f9",
            }}
          >
            <input
              accept="application/pdf"
              style={{ display: "none" }}
              id="pdf-file-upload"
              type="file"
              onChange={handleFileChange}
            />
            <label htmlFor="pdf-file-upload">
              <Button
                variant="outlined"
                component="span"
                startIcon={<UploadFileIcon />}
                sx={{ mb: 2 }}
              >
                PDF 파일 선택
              </Button>
            </label>

            <Typography variant="body2" sx={{ mt: 1 }}>
              {selectedFile ? selectedFile.name : "선택된 파일이 없습니다."}
            </Typography>
          </Box>

          <TextField
            select
            fullWidth
            label="카테고리"
            variant="outlined"
            size="small"
            sx={{ mb: 3 }}
            value={categoryId}
            onChange={handleCategoryChange}
          >
            <ListSubheader>가전제품 (가정용 전자기기)</ListSubheader>
            <MenuItem value="1">텔레비전 (TV)</MenuItem>
            <MenuItem value="2">냉장고</MenuItem>
            <MenuItem value="3">세탁기</MenuItem>
            <MenuItem value="4">전자레인지</MenuItem>
            <MenuItem value="5">에어컨</MenuItem>
            <MenuItem value="6">청소기 (유선/무선)</MenuItem>
            <MenuItem value="7">정수기</MenuItem>
            <MenuItem value="8">커피머신</MenuItem>
            <MenuItem value="9">전기밥솥</MenuItem>
            
            <ListSubheader>개인용 전자기기</ListSubheader>
            <MenuItem value="10">스마트폰</MenuItem>
            <MenuItem value="11">태블릿</MenuItem>
            <MenuItem value="12">노트북</MenuItem>
            <MenuItem value="13">스마트워치</MenuItem>
            <MenuItem value="14">이어폰/헤드폰 (유선/무선)</MenuItem>
            <MenuItem value="15">전자책 리더기</MenuItem>
          </TextField>

          {isUploading && (
            <Box sx={{ width: '100%', mb: 2 }}>
              <Typography variant="body2" sx={{ mb: 1, textAlign: 'center' }}>
                업로드 중... {Math.round(uploadProgress)}%
              </Typography>
              <LinearProgress 
                variant="determinate" 
                value={uploadProgress} 
                sx={{
                  height: 8,
                  borderRadius: 4,
                  '& .MuiLinearProgress-bar': {
                    backgroundColor: '#f4c542'
                  }
                }}
              />
            </Box>
          )}

          <Stack direction="row" spacing={2} justifyContent="flex-end">
            <Button variant="outlined" onClick={handleCloseModal}>
              취소
            </Button>
            <Button
              variant="contained"
              startIcon={<CloudUploadIcon />}
              onClick={handleSubmit}
              disabled={isUploading}
              sx={{
                backgroundColor: "#f4c542",
                color: "black",
                "&:hover": { backgroundColor: "#e0b73a" },
              }}
            >
              {isUploading ? "업로드 중..." : "등록하기"}
            </Button>
          </Stack>
        </Paper>
      </Modal>

      {/* 업로드 진행 상황 모달 */}
      <Modal
        open={isUploading}
        onClose={() => {}} // 업로드 중에는 닫을 수 없음
        aria-labelledby="upload-progress-modal"
        container={document.body}
      >
        <Paper
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: "85%",
            maxWidth: "350px",
            backgroundColor: "background.paper",
            boxShadow: 24,
            p: 4,
            borderRadius: 2,
            textAlign: "center",
          }}
        >
          <Typography variant="h6" component="h2" sx={{ mb: 3 }}>
            PDF 설명서 업로드 중
          </Typography>
          
          <Box sx={{ width: "100%", mb: 2 }}>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {uploadProgress < 100 
                ? "파일을 업로드하고 있습니다..."
                : "처리를 완료하고 있습니다..."}
            </Typography>
            <LinearProgress
              variant="determinate"
              value={uploadProgress}
              sx={{
                height: 10,
                borderRadius: 5,
                backgroundColor: '#e0e0e0',
                '& .MuiLinearProgress-bar': {
                  backgroundColor: '#f4c542',
                  borderRadius: 5,
                }
              }}
            />
            <Typography variant="body2" sx={{ mt: 1, color: 'text.secondary' }}>
              {Math.round(uploadProgress)}%
            </Typography>
          </Box>
          
          <Typography variant="caption" color="text.secondary">
            파일 크기에 따라 수 분이 소요될 수 있습니다
          </Typography>
        </Paper>
      </Modal>
    </Box>
  );
};

export default SideLayout;
