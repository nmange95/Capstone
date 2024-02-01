import React, { createContext, useContext, useState, useEffect } from "react";
import axiosBase from './axiosBase';
import { useAuth } from "./AuthContext";

const NotificationsContext = createContext();

export const NotificationsProvider = ({ children }) => {
  const [notifications, setNotifications] = useState([]);
  const { user } = useAuth();


  const fetchNotifications = async () => {
    try {
      const response = await axiosBase.get(`/api/notifications/user/${user.id}`);
      setNotifications(response.data);
    } catch (error) {
      console.error('Error fetching notifications:', error);
    }
  };

  useEffect(() => {
    if (user) {
      fetchNotifications();
    }
  }, [user]);

  useEffect(() => {
    const updateNotificationsOnServer = async () => {
      try {
        await axiosBase.put(`/api/notifications/user/${user.id}`, notifications);
      } catch (error) {
        console.error('Error updating notifications:', error);
      }
    };

    if (notifications.length > 0) {
      updateNotificationsOnServer();
    }
  }, [notifications]);

  const addNotification = (notification) => {
    setNotifications((prevNotifications) => [
      ...prevNotifications,
      notification,
    ]);
  };

  const clearNotifications = () => {
    setNotifications([]);
  };

  const markNotificationAsSeen = (notificationId) => {
    setNotifications((prevNotifications) => {
      const isAlreadySeen = prevNotifications.some(notification =>
        notification.id === notificationId && notification.seen);

      if (isAlreadySeen) {
        return prevNotifications;
      }

      return prevNotifications.map((notification) =>
        notification.id === notificationId
          ? { ...notification, seen: true }
          : notification
      );
    });
  };

  const deleteNotification = async (notificationId) => {
    try {
      await axiosBase.delete(`/api/notifications/${notificationId}`);
      setNotifications((prevNotifications) =>
        prevNotifications.filter((notification) => notification.id !== notificationId)
      );
    } catch (error) {
      console.error('Error deleting notification:', error);
    }
  };

  return (
    <NotificationsContext.Provider
      value={{
        notifications,
        addNotification,
        clearNotifications,
        markNotificationAsSeen,
        fetchNotifications,
        deleteNotification,
      }}
    >
      {children}
    </NotificationsContext.Provider>
  );
};

export const useNotifications = () => {
  return useContext(NotificationsContext);
};
