import React, { useEffect, useState } from 'react';
import { Snackbar, Alert } from '@mui/material';
import { useSelector } from 'react-redux';
import '../styles/Notification.css';

const ToastNotification = () => {
  const notifications = useSelector((state) => state.notification.notifications);
  const [open, setOpen] = useState(false);
  const [latestNotification, setLatestNotification] = useState(null);

  useEffect(() => {
    if (notifications.length > 0) {
      const newest = notifications[0];
      
      if (newest && newest.read === false) {
        setLatestNotification(newest);
        setOpen(true);
      }
    }
  }, [notifications]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Snackbar
      open={open}
      autoHideDuration={5000}
      onClose={handleClose}
      anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
    >
      <Alert 
        onClose={handleClose} 
        severity="success" 
        variant="filled"
        sx={{ width: '100%' }}
        className="toast-notification"
      >
        {latestNotification?.title}: {latestNotification?.message}
      </Alert>
    </Snackbar>
  );
};

export default ToastNotification;
