import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import CloseIcon from "@mui/icons-material/Close";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import {
  Box,
  Button,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Modal,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logout } from "../redux/LoginSlice";

const SideLayout = ({ onClose }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [openModal, setOpenModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [deviceName, setDeviceName] = useState("");

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedFile(null);
    setDeviceName("");
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file && file.type === "application/pdf") {
      setSelectedFile(file);
    } else {
      alert("PDF 파일만 업로드 가능합니다.");
    }
  };

  const handleSubmit = () => {
    if (!selectedFile) {
      alert("PDF 파일을 선택해주세요.");
      return;
    }

    if (!deviceName.trim()) {
      alert("제품명을 입력해주세요.");
      return;
    }

    console.log("업로드할 파일:", selectedFile);
    console.log("제품명:", deviceName);

    handleCloseModal();
  };

  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
  };

  const handleDeviceClick = (deviceName) => {
    navigate("/chat", { state: { deviceName } });
    onClose();
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
          <ListItem
            sx={{ padding: "4px 0", cursor: "pointer" }}
            onClick={() => handleDeviceClick("Samsung S24")}
          >
            <ListItemText primary="Samsung S24" />
          </ListItem>
          <ListItem
            sx={{ padding: "4px 0", cursor: "pointer" }}
            onClick={() => handleDeviceClick("Samsung DDD")}
          >
            <ListItemText primary="Samsung DDD" />
          </ListItem>
          <ListItem
            sx={{ padding: "4px 0", cursor: "pointer" }}
            onClick={() => handleDeviceClick("JJJ")}
          >
            <ListItemText primary="JJJ" />
          </ListItem>
          <ListItem
            sx={{ padding: "4px 0", cursor: "pointer" }}
            onClick={() => handleDeviceClick("NNN")}
          >
            <ListItemText primary="NNN" />
          </ListItem>
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
      >
        <Paper
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: "85%",
            maxWidth: "400px",
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

          <Stack direction="row" spacing={2} justifyContent="flex-end">
            <Button variant="outlined" onClick={handleCloseModal}>
              취소
            </Button>
            <Button
              variant="contained"
              startIcon={<CloudUploadIcon />}
              onClick={handleSubmit}
              sx={{
                backgroundColor: "#f4c542",
                color: "black",
                "&:hover": { backgroundColor: "#e0b73a" },
              }}
            >
              등록하기
            </Button>
          </Stack>
        </Paper>
      </Modal>
    </Box>
  );
};

export default SideLayout;
