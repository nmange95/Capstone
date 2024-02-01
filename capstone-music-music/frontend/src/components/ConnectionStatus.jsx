import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import axiosBase from '../contexts/axiosBase';

const ConnectionStatus = () => {
    const [status, setStatus] = useState('Checking...');
    const { user } = useAuth();
    const token = user?.token;

    useEffect(() => {
        if (token) {
            axiosBase.get('/api/auth/checkStatus', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then(({ status }) => {
                    setStatus(status === 200 ? 'Connected' : 'Not Connected');
                })
                .catch((error) => {
                    console.error('An error occurred:', error);
                    setStatus('Not Connected');
                });
        } else {
            setStatus('Not Connected');
        }
    }, [token]);


    return (
        <div>
            {status}
        </div>
    );
};

export default ConnectionStatus;