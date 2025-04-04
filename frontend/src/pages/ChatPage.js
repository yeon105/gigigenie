import React, { useState } from "react";
import { Box, Typography } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import "../styles/ChatPage.css";
import { useLocation } from "react-router-dom";

const ChatPage = () => {
  const location = useLocation();
  const { deviceName = "Galaxy S23 Ultra" } = location.state || {};

  const [messages, setMessages] = useState([
    {
      role: "bot",
      text: "안녕하세요. 어떤 사용법을 알려드릴까요?",
    },
    {
      role: "user",
      text: "갤럭시 S23 Ultra에서 나이트 모드 사진 찍는 방법을 알려주세요.",
    },
    {
      role: "bot",
      text: "갤럭시 S23 Ultra에서 나이트 모드 사진 촬영 방법을 알려드리겠습니다.\n\n1. 카메라 앱을 실행하세요.\n2. 화면 모드 옵션에서 더 보기를 선택하세요.\n3. 나이트 모드를 선택하세요.\n4. 촬영 버튼을 눌러 사진을 찍으세요.",
    },
    {
      role: "user",
      text: "나이트 모드 활성 시 팁이 있을까요?",
    },
  ]);
  const [input, setInput] = useState("");

  const handleSend = () => {
    if (!input.trim()) return;
    setMessages([
      ...messages,
      { role: "user", text: input },
      { role: "bot", text: "AI 응답 예제" },
    ]);
    setInput("");
  };

  return (
    <Box className="chat-main">
      <Box className="chat-header">{deviceName}</Box>

      <Box className="chat-messages">
        {messages.map((msg, idx) => (
          <Box key={idx} className={`message ${msg.role}`}>
            {msg.text.split("\n").map((line, i) => (
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
            ))}
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
            onKeyDown={(e) => e.key === "Enter" && handleSend()}
          />
          <button className="input-send-button" onClick={handleSend}>
            <SendIcon fontSize="small" />
          </button>
        </div>
      </Box>
    </Box>
  );
};

export default ChatPage;
