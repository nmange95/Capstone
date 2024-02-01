import React, { useEffect, useState } from "react";
import { UserView , Portfolio } from "../components/ViewOnlyProfile";
import MusicTagsDisplayViewOnly from "../components/MusicTagsDisplayViewOnly";
import axiosBase from "../contexts/axiosBase";
import { useParams } from "react-router-dom";
import "./ProfilePage.css";

const ViewUserProfile = () => {
  const [selectedUser, setSelectedUser] = useState(null);
  const { username } = useParams();
  const [userAudios, setUserAudios] = useState([])

  useEffect(() => {
    axiosBase.get(`/api/users/search?username=${username}`)
      .then(response => {
        setSelectedUser(response.data);
        fetchUserAudios(response.data.id);
      })
      .catch(error => {
        console.error("Error fetching user data:", error);
      });
  }, [username]);
  
  const fetchUserAudios = async (userId) => {
    try {
      const response = await axiosBase.get(`/api/audio/user/${userId}`);
      setUserAudios(response.data);
    } catch (error) {
      console.error("Error fetching user audios:", error);
    }
  };
  if (!selectedUser) {
    return <p>Loading...</p>;
  }

  return (
    <div className="main-container">
      <div className="display-profile-container">
        <UserView selectedUser={selectedUser} />
        <MusicTagsDisplayViewOnly data={selectedUser.musicTags} />
        {/* <Portfolio userId={selectedUser.id} />  */}
        {/* ^ table from ViewOnlyProfile.jsx is not needed since we have the section below*/}
        </div>

        <div className="audio-list">
        <h2>{`${selectedUser.username}'s Audio Uploads`}</h2>
        {userAudios.map((audio) => (
          <div key={audio.id} className="audio-item">
            <h3>{audio.title}</h3>
            <p>Artist: {audio.artist}</p>
            <p>Genre: {audio.genre}</p>
            <audio controls>
              <source src={`http://localhost:8080/api/audio/stream/${audio.id}`} type="audio/mpeg" />
              Your browser does not support the audio tag.
            </audio>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ViewUserProfile;
