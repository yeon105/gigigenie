import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import CloseIcon from "@mui/icons-material/Close";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import ImageIcon from "@mui/icons-material/Image";
import {
  Box,
  Button,
  IconButton,
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
  RadioGroup,
  FormControlLabel,
  Radio,
  FormControl,
  FormLabel,
} from "@mui/material";
import React, { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { savePdf } from "../api/chatApi";
import "../styles/SideLayout.css";
import { addNotification, showToastMessage } from "../redux/NotificationSlice";

const SideLayout = ({ onClose, onProductUpdate }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [openModal, setOpenModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [deviceName, setDeviceName] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [isUploading, setIsUploading] = useState(false);
  const [uploadImage, setUploadImage] = useState("no");
  const [selectedImage, setSelectedImage] = useState(null);
  const userFavorites = useSelector((state) => state.login.favoriteList) || [];
  const products = useSelector((state) => state.product.products) || [];

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
      setUploadImage("no");
      setSelectedImage(null);
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

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const validImageTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/webp'];
      if (validImageTypes.includes(file.type)) {
        setSelectedImage(file);
      } else {
        alert("JPG, JPEG, PNG, WEBP 형식의 이미지만 업로드 가능합니다.");
        setSelectedImage(null);
      }
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

    if (uploadImage === "yes" && !selectedImage) {
      alert("이미지를 선택해주세요.");
      return;
    }

    setIsUploading(true);
    setOpenModal(false);

    try {
      const response = await savePdf(
        deviceName, 
        categoryId, 
        selectedFile, 
        uploadImage === "yes" ? selectedImage : null
      );
      
      if (response.status === "exists") {
        alert(`${response.model_name}은(는) 이미 등록된 모델입니다.`);
      } else {
        await new Promise(resolve => setTimeout(resolve, 500));
        await onProductUpdate();
        
        dispatch(addNotification({
          title: "제품 등록 완료",
          message: `${deviceName} 설명서가 성공적으로 등록되었습니다.`,
          fontSize: 'small'
        }));
      }
    } catch (error) {
      alert("제품 설명서 등록에 실패했습니다.");
      console.error("등록 실패:", error);
    } finally {
      setTimeout(() => {
        setIsUploading(false);
        handleCloseModal();
      }, 1000);
    }
  };

  const handleDeviceClick = (device) => {
    navigate("/chat", { state: { deviceName: device.name, productId: device.id } });
    onClose();
  };

  const handleCategoryChange = (event) => {
    setCategoryId(event.target.value);
  };

  return (
    <Box className="sidebar">
      <IconButton
        onClick={onClose}
        className="sidebar-close-btn"
      >
        <CloseIcon />
      </IconButton>

      <Box className="sidebar-content">
        <Typography
          variant="subtitle1"
          className="sidebar-title"
        >
          즐겨찾기 (내 디바이스)
        </Typography>

        <List className="device-list">
          {favoriteProducts.map((product) => (
            <ListItem
              key={product.id}
              className="device-item"
              onClick={() => handleDeviceClick(product)}
            >
              <ListItemText primary={product.name} />
            </ListItem>
          ))}
        </List>
      </Box>

      <Box className="bottom-actions">
        <Box sx={{ marginBottom: "15px" }}>
          <Typography
            variant="body2"
            className="register-hint"
          >
            등록을 원하는 제품의 PDF 설명서를 등록하세요!
          </Typography>
          <Button
            fullWidth
            variant="contained"
            startIcon={<AddCircleOutlineIcon />}
            onClick={handleOpenModal}
            disabled={isUploading}
            className="register-btn"
          >
            {isUploading ? '등록 중...' : '등록'}
          </Button>
          {isUploading && (
            <Typography
              variant="caption"
              sx={{ mt: 1, textAlign: 'center', display: 'block', color: '#00c471' }}
            >
              제품 설명서를 처리 중입니다...
            </Typography>
          )}
        </Box>
      </Box>

      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="pdf-upload-modal"
        container={document.body}
      >
        <Paper className="upload-modal">
          <Box className="modal-header">
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

          <Box className="file-upload-area">
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
                className="file-upload-btn"
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

          <FormControl component="fieldset" sx={{ mb: 3 }}>
            <FormLabel component="legend">제품 이미지 업로드</FormLabel>
            <RadioGroup
              row
              name="image-upload-options"
              value={uploadImage}
              onChange={(e) => setUploadImage(e.target.value)}
            >
              <FormControlLabel value="yes" control={<Radio />} label="등록" />
              <FormControlLabel value="no" control={<Radio />} label="등록안함" />
            </RadioGroup>
          </FormControl>

          {uploadImage === "yes" && (
            <Box className="file-upload-area" sx={{ mb: 3 }}>
              <input
                accept="image/jpeg,image/png,image/jpg,image/webp"
                style={{ display: "none" }}
                id="image-file-upload"
                type="file"
                onChange={handleImageChange}
              />
              <label htmlFor="image-file-upload">
                <Button
                  variant="outlined"
                  component="span"
                  startIcon={<ImageIcon />}
                  className="file-upload-btn"
                >
                  이미지 선택
                </Button>
              </label>

              <Typography variant="body2" sx={{ mt: 1 }}>
                {selectedImage ? selectedImage.name : "선택된 이미지가 없습니다."}
              </Typography>
              {selectedImage && (
                <Typography variant="caption" sx={{ display: 'block', mt: 0.5, color: 'text.secondary' }}>
                  지원 형식: JPG, JPEG, PNG, WEBP
                </Typography>
              )}
            </Box>
          )}

          <Stack className="action-buttons">
            <Button
              variant="contained"
              startIcon={<CloudUploadIcon />}
              onClick={() => {
                dispatch(showToastMessage("등록 완료 시 알림으로 안내됩니다."));
                setTimeout(() => handleSubmit(), 100);
              }}
              disabled={isUploading}
              className="submit-btn"
            >
              {isUploading ? '등록 중...' : '등록하기'}
            </Button>
          </Stack>
        </Paper>
      </Modal>
    </Box>
  );
};

export default SideLayout;
