import React from 'react';
import { 
  Box, 
  Popover, 
  List, 
  ListItem, 
  ListItemText, 
  Typography, 
  IconButton, 
  Badge 
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import '../styles/Notification.css';

const NotificationComponent = ({ 
  anchorEl, 
  open, 
  onClose, 
  notifications, 
  onClearNotification 
}) => {
  if (notifications.length === 0) {
    return (
      <Popover
        open={open}
        anchorEl={anchorEl}
        onClose={onClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        className="notification-popover"
      >
        <Box className="empty-notification">
          <Typography variant="body2">알림이 없습니다.</Typography>
        </Box>
      </Popover>
    );
  }

  return (
    <Popover
      open={open}
      anchorEl={anchorEl}
      onClose={onClose}
      anchorOrigin={{
        vertical: 'bottom',
        horizontal: 'right',
      }}
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      className="notification-popover"
    >
      <List className="notification-list">
        {notifications.map((notification) => (
          <ListItem 
            key={notification.id} 
            className="notification-item"
            secondaryAction={
              <IconButton 
                edge="end" 
                aria-label="delete" 
                onClick={() => onClearNotification(notification.id)}
                size="small"
              >
                <CloseIcon fontSize="small" />
              </IconButton>
            }
          >
            <CheckCircleIcon color="success" className="notification-icon" />
            <ListItemText
              primary={
                <Typography
                  variant={notification.fontSize === 'small' ? 'body2' : 'body1'}
                  sx={{ fontWeight: 500 }}
                >
                  {notification.title ? `${notification.title}: ${notification.message}` : notification.message}
                </Typography>
              }
              secondary={
                <Typography
                  component="span"
                  variant="caption"
                  display="block"
                  className="notification-time"
                >
                  {notification.time}
                </Typography>
              }
            />
          </ListItem>
        ))}
      </List>
    </Popover>
  );
};

export default NotificationComponent;
