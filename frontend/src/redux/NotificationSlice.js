import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  notifications: [],
  toastMessage: '',
  showToast: false,
};

const notificationSlice = createSlice({
  name: 'notification',
  initialState,
  reducers: {
    addNotification: (state, action) => {
      state.notifications.unshift({
        id: Date.now(),
        message: action.payload.message,
        title: action.payload.title || '',
        time: new Date().toLocaleTimeString(),
        read: false,
        fontSize: action.payload.fontSize || 'normal'
      });
    },
    removeNotification: (state, action) => {
      state.notifications = state.notifications.filter(
        (notification) => notification.id !== action.payload
      );
    },
    clearAllNotifications: (state) => {
      state.notifications = [];
    },
    markAllAsRead: (state) => {
      state.notifications = state.notifications.map(notification => ({
        ...notification,
        read: true
      }));
    },
    showToastMessage: (state, action) => {
      state.toastMessage = action.payload;
      state.showToast = true;
    },
    hideToastMessage: (state) => {
      state.showToast = false;
    }
  },
});

export const { 
  addNotification, 
  removeNotification, 
  clearAllNotifications,
  markAllAsRead,
  showToastMessage,
  hideToastMessage
} = notificationSlice.actions;

export default notificationSlice.reducer;
