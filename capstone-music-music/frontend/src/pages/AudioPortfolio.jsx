import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../contexts/AuthContext";
import { Link } from "react-router-dom";
import { format } from "date-fns";

const AudioPortfolio = () => {
  const { user } = useAuth();
  const [audios, setAudios] = useState([]);

  useEffect(() => {
    if (user) {
      fetchAudios();
    }
  }, [user]);

  const fetchAudios = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/audio/user/${user.id}`,
        {
          headers: {
            Authorization: `Bearer ${user.token}`,
          },
        }
      );
      setAudios(response.data);
    } catch (error) {
      console.error("Error fetching audios:", error);
    }
  };

  return (
    <div className="main-container">
      <h2>Your Audio Uploads</h2>
      <div className="audio-list">
        {audios.map((audio) => (
          <div key={audio.id} className="audio-item">
            <h3>{audio.title}</h3>
            <p>Artist: {audio.artist}</p>
            <p>Genre: {audio.genre}</p>
            <audio controls>
              <source src={`http://localhost:8080/api/audio/stream/${audio.id}`} type="audio/mpeg" />
              Your browser does not support the audio tag.
            </audio>
            <p>
              Upload Date:{" "}
              {audio.uploadDate
                ? format(new Date(audio.uploadDate), "yyyy-MM-dd")
                : ""}
            </p>
          </div>
        ))}
      </div>
      <Link to="/portfolio/upload">Upload New Audio</Link>
    </div>
  );
};

export default AudioPortfolio;
