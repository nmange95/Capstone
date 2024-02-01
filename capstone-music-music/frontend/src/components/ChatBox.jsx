import React, { useState, useEffect, useRef, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from 'stompjs/lib/stomp.min.js'
import { useAuth } from '../contexts/AuthContext';
import axiosBase from '../contexts/axiosBase';
import { useNotifications } from '../contexts/NotificationsContext';


const ChatBox = () => {
    const [stompClient, setStompClient] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const [friends, setFriends] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [isExpanded, setIsExpanded] = useState(false);
    const friendsRef = useRef([]);
    const messagesEndRef = useRef(null);
    const { user } = useAuth();
    const { fetchNotifications } = useNotifications();
    const fetchNotificationsRef = useRef(fetchNotifications);
    const [refreshFriends, setRefreshFriends] = useState(false);

    const handleUserSelect = useCallback((user) => {
        setSelectedUser(user);
    }, []);

    const fetchMessageHistory = useCallback(async () => {
        if (selectedUser) {
            axiosBase.get(`/api/message/history?user1=${user.username}&user2=${selectedUser.username}`)
                .then(({ data, status }) => {
                    if (status === 200) {
                        setMessages(Array.isArray(data) ? data : []);
                    } else {
                        console.error('Failed to fetch message history');
                    }
                })
                .catch((error) => {
                    console.error('Failed to fetch message history', error);
                });
        }
    }, [selectedUser, user.username]);

    const handleIncomingMessage = useCallback((incomingMessage) => {
        const senderUsername = incomingMessage.sender;
        console.log("friends: " + friendsRef.current);
        const senderUser = friendsRef.current.find(friend => friend.username === senderUsername);
        console.log("sender user: " + senderUser)
        console.log("selected User: " + (selectedUser ? selectedUser.username : "null"));

        if (senderUser) {
            if (!isExpanded) {
                console.log("expanding chat.")
                setIsExpanded(true);
                handleUserSelect(senderUser);
            }

            if (!selectedUser || selectedUser.username !== senderUsername) {
                console.log("changing selected user");
                handleUserSelect(senderUser);
            }

            fetchMessageHistory(senderUser);
        }
    }, [isExpanded, selectedUser, friendsRef, handleUserSelect, fetchMessageHistory]);


    const toggleChatBox = () => {
        setIsExpanded(!isExpanded);
    }

    const loadFriends = useCallback(() => {
        if (user && (user.id !== undefined)) {
            axiosBase.get(`/api/users/${user.id}/friends`)
                .then(response => setFriends(response.data))
                .catch(error => console.error('Error fetching friends:', error));
        }
        console.log("friends loaded")
    }, [user]);

    useEffect(() => {
        loadFriends();
    }, [user, loadFriends]);

    useEffect(() => {
        if (refreshFriends) {
            loadFriends();
            setRefreshFriends(false);
        }
    }, [refreshFriends, user, loadFriends]);

    useEffect(() => {
        const socket = new SockJS(`http://localhost:8080/chat`);
        const client = Stomp.over(socket);

        let isConnected = false;
        console.log('Current token: ' + user.token);

        client.connect({
            'Authorization': `Bearer ${user.token}`
        }, frame => {
            isConnected = true;
            setStompClient(client);
            client.subscribe('/user/queue/messages', message => {
                const incomingMessage = JSON.parse(message.body);
                handleIncomingMessage(incomingMessage);
            });

            client.subscribe('/user/queue/notifications', message => {
                const notificationMsg = message.body;
                if (notificationMsg.includes('UPDATE')) {
                    fetchNotificationsRef.current();
                    setRefreshFriends(true);
                }
            });
        });

        return () => {
            if (client && isConnected) {
                client.disconnect();
            }
        };
    }, [handleIncomingMessage, user.token]);




    useEffect(() => {
        fetchMessageHistory();
    }, [selectedUser, user.username, fetchMessageHistory]);


    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "auto" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    useEffect(() => {
        friendsRef.current = friends;
        console.log("friendsRef refreshed")
    }, [friends]);

    useEffect(() => {
        fetchNotificationsRef.current = fetchNotifications;
    }, [fetchNotifications]);




    const sendMessage = async () => {
        if (stompClient && newMessage && selectedUser) {
            const chatMessage = { sender: user.username, recipient: selectedUser.username, text: newMessage };
            stompClient.send("/api/message", { 'Authorization': `Bearer ${user.token}` }, JSON.stringify(chatMessage));
            setNewMessage("");
            await fetchMessageHistory();
        }
    };

    return (
        <div className={isExpanded ? "chatbox" : "chatbox-collapsed"}>
            {isExpanded ? (
                <>
                    <div className="chatbox-header">
                        <div className="chatbox-header-arrow-space">
                            {selectedUser ? (
                                <div className="chatbox-back-arrow" onClick={() => setSelectedUser(null)}>
                                    &#8592; {/* Left arrow */}
                                </div>
                            ) : (
                                <div className="chatbox-back-arrow">&nbsp;</div>
                            )}
                        </div>
                        <div className="chatbox-title" onClick={toggleChatBox}>
                            {selectedUser ? `Chatting with: ${selectedUser.username}` : "Messages"}
                        </div>
                    </div>
                    {selectedUser ? (
                        <>
                            <div className='chatbox-content'>
                                {messages.map((msg) => (
                                    <div key={msg.id}>
                                        <span style={{ fontWeight: 'bold' }}>{msg.sender}:</span> {msg.text}
                                    </div>
                                ))}
                                {/* Invisible element for auto scroll down */}
                                <div ref={messagesEndRef} />
                            </div>
                            <input
                                className="chatbox-input"
                                type="text"
                                value={newMessage}
                                onChange={e => setNewMessage(e.target.value)}
                                onKeyDown={e => { if (e.key === 'Enter') sendMessage(); }}
                            />
                            <button onClick={sendMessage}>Send</button>
                        </>
                    ) : (
                        <div className="chatbox-user-selection-container">
                            Select a user to chat with:
                            {friends.map((friend) => (
                                <div key={friend.id} onClick={() => handleUserSelect(friend)} className="chatbox-user-item">
                                    {friend.username}
                                </div>
                            ))}
                        </div>
                    )}
                </>
            ) : (
                <div onClick={toggleChatBox}>Messages</div>
            )}
        </div>
    );
}

export default ChatBox;
