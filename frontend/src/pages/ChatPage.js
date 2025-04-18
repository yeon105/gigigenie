import React, { useState } from "react";
import { 
  Box, 
  Typography, 
  CircularProgress, 
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Stack
} from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import "../styles/ChatPage.css";
import { useLocation, useNavigate } from "react-router-dom";
import { createAnswer } from '../api/chatApi';

const ChatPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { deviceName, productId } = location.state || {};
  const [openExitDialog, setOpenExitDialog] = useState(false);

  // 제품 정보가 없는 경우 메인 페이지로 리다이렉트
  React.useEffect(() => {
    if (!deviceName || !productId) {
      navigate("/");
    }
  }, [deviceName, productId, navigate]);

  const [isLoading, setIsLoading] = useState(false);
  const [messages, setMessages] = useState([
    {
      role: "bot",
      text: "안녕하세요. 어떤 사용법을 알려드릴까요?",
    },
  ]);
  const [input, setInput] = useState("");

  const handleSend = async () => {
    if (!input.trim()) return;

    setMessages(prev => [...prev, { role: "user", text: input }]);
    
    setMessages(prev => [...prev, { role: "bot", isLoading: true }]);
    setIsLoading(true);
    
    try {
      const collectionName = productId ? `product_${productId}_embeddings` : "langchain";
      const response = await createAnswer(
        input,
        collectionName,
        3
      );

      setMessages(prev => [
        ...prev.slice(0, -1),
        { role: "bot", text: response.answer }
      ]);
    } catch (error) {
      console.error("답변 생성 실패:", error);
      setMessages(prev => [
        ...prev.slice(0, -1),
        { role: "bot", text: "죄송합니다. 답변을 생성하는 중에 문제가 발생했습니다." }
      ]);
    } finally {
      setIsLoading(false);
    }

    setInput("");
  };

  const handleExitClick = () => {
    setOpenExitDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenExitDialog(false);
  };

  const handleConfirmExit = () => {
    navigate('/');
  };

  return (
    <Box className="chat-main">
      <Box className="chat-header">
        <Typography>{deviceName}</Typography>
        <Button 
          onClick={handleExitClick}
          variant="text"
          className="exit-button"
        >
          채팅방 나가기
        </Button>
      </Box>

      <Dialog
        open={openExitDialog}
        onClose={handleCloseDialog}
        PaperProps={{
          className: "exit-dialog-paper"
        }}
      >
        <DialogTitle className="exit-dialog-title">
          채팅방 나가기
        </DialogTitle>
        <DialogContent>
          <Typography className="exit-dialog-content">
            채팅 내역을 저장하시겠습니까?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Stack 
            direction="row" 
            spacing={1}
            className="dialog-buttons"
          >
            <Button
              variant="contained"
              onClick={handleCloseDialog}
              className="save-button"
            >
              저장하기
            </Button>
            <Button
              variant="contained"
              onClick={handleConfirmExit}
              className="exit-button-dialog"
            >
              나가기
            </Button>
            <Button
              variant="outlined"
              onClick={handleCloseDialog}
              className="cancel-button"
            >
              취소
            </Button>
          </Stack>
        </DialogActions>
      </Dialog>

      <Box className="chat-messages">
        {messages.map((msg, idx) => (
          <Box key={idx} className={`message ${msg.role}`}>
            {msg.isLoading ? (
              <Box className="loading-container">
                <CircularProgress 
                  size={20} 
                  className="loading-spinner"
                />
                <Typography>답변 생성 중...</Typography>
              </Box>
            ) : (
              msg.text.split("\n").map((line, i) => (
                <Typography
                  key={i}
                  component={i === 0 ? "div" : "p"}
                  sx={{
                    margin: i === 0 ? 0 : "4px 0",
                    whiteSpace: "pre-wrap",
                  }}
                >
                  {line}
                </Typography>
              ))
            )}
          </Box>
        ))}
      </Box>

      <Box className="chat-input">
        <div className="input-container">
          <input
            type="text"
            placeholder="해당 제품의 궁금한 점을 질문하세요!"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && !isLoading && handleSend()}
            disabled={isLoading}
          />
          <button 
            className="input-send-button" 
            onClick={handleSend}
            disabled={isLoading}
          >
            <SendIcon fontSize="small" />
          </button>
        </div>
      </Box>
    </Box>
  );
};

export default ChatPage;
