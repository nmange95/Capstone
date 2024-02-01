import React from 'react';
import { useModal } from '../contexts/ModalContext';
import { useAuth } from "../contexts/AuthContext";

const LogoutModal = ({ isOpen, onClose }) => {
    const { logout } = useAuth();
    const { hideModal } = useModal();

    const handleLogout = async () => {
        try {
            await logout();
            hideModal();
        } catch (error) {
            console.error('Failed to log out:', error);
        }
    };
    return (
        <>
            <h2>Log Out</h2>
            <p>Are you sure you want to log out?</p>
            <button onClick={handleLogout}>Yes, Log out</button>
            <button onClick={hideModal}>Cancel</button>
        </>
    );
};

export default LogoutModal;