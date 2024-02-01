import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useNotifications } from "../contexts/NotificationsContext.js";
import axiosBase from "../contexts/axiosBase";

const NotificationsMenu = () => {
    const { notifications, markNotificationAsSeen, deleteNotification } = useNotifications();
    const [isDropdownVisible, setDropdownVisible] = useState(false);
    const [selectedMessage, setSelectedMessage] = useState(null);

    const handleNotificationClick = () => {
        setDropdownVisible(true);
        setSelectedMessage(null);
    };

    const handleMessageClick = async (index, notificationId) => {
        setSelectedMessage(index);
        await markNotificationAsSeen(notificationId);
    };

    const handleAccept = async (notificationId, pairRequestId) => {
        try {
            await axiosBase.post(`/api/pair-requests/respond`, null, {
                params: { pairRequestId, response: "ACCEPTED" }
            });

            await deleteNotification(notificationId);
        } catch (error) {
            console.error('Error accepting pair request:', error);
        }
    };

    const handleDecline = async (notificationId, pairRequestId) => {
        try {
            await axiosBase.post(`/api/pair-requests/respond`, null, {
                params: { pairRequestId, response: "REJECTED" }
            });

            await deleteNotification(notificationId);
        } catch (error) {
            console.error('Error declining pair request:', error);
        }
    };

    return (
        <div className="notification-container">
            <button className="notification-button" onMouseEnter={handleNotificationClick}>
                {notifications.filter((n) => !n.seen).length > 0 && (
                    <div className="notification-badge">
                        {notifications.filter((n) => !n.seen).length}
                    </div>
                )}
                Notifications
            </button>
            {isDropdownVisible && (
                <div className="notification-dropdown">
                    {notifications.length > 0 ? (
                        notifications.map((notification, index) => (
                            <div
                                key={index}
                                className={`notification-item ${selectedMessage === index ? "selected" : ""
                                    }`}
                                onMouseEnter={() => handleMessageClick(index, notification.id)}
                            >
                                <p className="notification-message">
                                    You received a pair request from{" "}
                                    <Link to={`/user/${notification.request.sender.username}`} className="notification-sender-link">
                                        {notification.request.sender.username}
                                    </Link>
                                </p>
                                {selectedMessage === index && (
                                    <div>
                                        <button className="notif-accept" onClick={() => handleAccept(notification.id, notification.request.id)}>Accept</button>
                                        <button className="notif-deny" onClick={() => handleDecline(notification.id, notification.request.id)}>Decline</button>

                                    </div>
                                )}
                            </div>
                        ))
                    ) : (
                        <p className="no-notifications-message">You have no notifications</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default NotificationsMenu;