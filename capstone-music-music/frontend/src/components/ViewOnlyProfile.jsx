import React, { useState, useEffect } from "react";
import { format } from "date-fns";
import { useModal } from "../contexts/ModalContext";
import { useAuth } from "../contexts/AuthContext";
import SendPairRequest from "./SendPairRequest";
import axiosBase from "../contexts/axiosBase";

export const UserView = ({ selectedUser }) => {
  const [isFriends, setIsFriends] = useState(null);
  const [requestSent, setRequestSent] = useState(false);
  const { showModal } = useModal();
  const { user } = useAuth();

  const checkFriendship = async () => {
    if (user && selectedUser) {
      try {
        const response = await axiosBase.get(`/api/users/${user.id}/friends`);
        const isFriend = response.data.some(friend => friend.id === selectedUser.id);
        setIsFriends(isFriend);
      } catch (error) {
        console.error("Error checking friendship:", error);
      }
    }
  };

  const checkForSentRequest = async () => {
    if (user && selectedUser) {
      try {
        const response = await axiosBase.get(`/api/pair-requests/pending/${user.username}/${selectedUser.username}`);
        setRequestSent(response.data.exists);
      } catch (error) {
        console.error("Error checking sent request:", error);
      }
    }
  };

  const refreshChecks = () => {
    checkFriendship();
    checkForSentRequest();
  };

  useEffect(() => {
    refreshChecks();
  }, [user, selectedUser]);


  if (!selectedUser) {
    return <p>Loading...</p>;
  }

  const handleSendPairRequest = () => {
    if (user && selectedUser) {
      showModal(<SendPairRequest senderUser={user} receiverUser={selectedUser} onSendSuccess={refreshChecks} />);
    } else if (user) {
      console.log("receiver user not loaded")
    } else if (selectedUser) {
      console.log("sender user not loaded")
    }

  };

  const formatEnum = (input) => {
    if (input) {
      return input.split('_')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(' ');
    } else {
      return '';
    }
  }

  const renderFriendshipStatus = () => {
    if (isFriends) {
      return <p>You are friends with this user.</p>;
    } else if (requestSent) {
      return <p>You've sent a pair request to this user, they haven't responded yet.</p>;
    } else {
      return <button onClick={handleSendPairRequest}>Send Pair Request</button>;
    }
  };

  return (
    <div className="profile-details-section">
      <div className="profile-header">
        <img src={selectedUser.imageURL} className="profile-picture" alt="Profile" />
        <h1 className="profile-username">{selectedUser.username}</h1>
      </div>
      <div className="profile-info">
        <h2>Instrument:</h2>
        <p>{selectedUser.instrument}</p>
      </div>
      <div className="profile-info">
        <h2>Experience:</h2>
        <p>{formatEnum(selectedUser.experienceLevel)}</p>
      </div>
      <div className="profile-info">
        <h2>Genre:</h2>
        <p>{selectedUser.genre}</p>
      </div>
      {renderFriendshipStatus()}
    </div>
  );
};

export const Portfolio = ({ userId }) => {
  const [audios, setAudios] = useState([]);


  useEffect(() => {
    const fetchAudiosOnUserDataChange = async () => {
      if (userId) {
        axiosBase.get(`/api/audio/user/${userId}`)
          .then(response => setAudios(response.data))
          .catch(error => console.error("Error fetching audios:", error));
      }
    };

    fetchAudiosOnUserDataChange();
  }, [userId]);


  if (!audios) {
    return <p>Loading...</p>;
  }


  return (
    <div className="profile-portfolio-section">
      <h2>Uploads</h2>
      <table>
        <thead>
          <tr>
            <th>Title</th>
            <th>Artist</th>
            <th>Genre</th>
            <th>Duration (s)</th>
            <th>Upload Date</th>
          </tr>
        </thead>
        <tbody>
          {audios.map((audio) => (
            <tr key={audio.id}>
              <td>{audio.title}</td>
              <td>{audio.artist}</td>
              <td>{audio.genre}</td>
              <td>{audio.duration}</td>
              <td>
                {audio.uploadDate
                  ? format(new Date(audio.uploadDate), "yyyy-MM-dd")
                  : ""}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
