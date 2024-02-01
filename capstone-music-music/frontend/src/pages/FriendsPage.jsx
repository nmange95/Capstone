import React, { useEffect, useState } from 'react';
import axiosBase from '../contexts/axiosBase';
import { useAuth } from '../contexts/AuthContext';
import { Link } from 'react-router-dom';

const FriendsPage = () => {
    const [friends, setFriends] = useState([]);
    const { user } = useAuth();

    useEffect(() => {
        const getFriendsOnLoad = () => {
            if (user && (user.id !== undefined)) {
                axiosBase.get(`/api/users/${user.id}/friends`)
                    .then(response => setFriends(response.data))
                    .catch(error => console.error('Error fetching friends:', error));
            }
        };

        console.log(user.experienceLevel);

        getFriendsOnLoad();
    }, [user, user.id]);


    return (
        <div className="main-container">
            <h1>Friends</h1>
            {friends.length > 0 ? (
                <div className="friend-list-container">
                    <ul>
                        {friends.map((friend) => (
                            <li key={friend.id}>
                                <img src={friend.imageURL} alt='otter wearing headphones' className="friends-profile-picture" />
                                <Link to={`/user/${friend.username}`}>{friend.username}</Link>
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <p>No friends yet.</p>
            )}
        </div>
    );
};

export default FriendsPage;
