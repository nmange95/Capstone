import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import { useAuth } from "../contexts/AuthContext";
import axiosBase from "../contexts/axiosBase";


const ProfileCreation = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [instrument, setInstrument] = useState("");
  const [genre, setGenre] = useState("");
  const [experienceLevel, setExperienceLevel] = useState("");
  const [imageURL, setImageURL] = useState("ExperienceLevelEnum.BEGINNER");
  const { user, login } = useAuth();
  const defaultProfilePics = [
    "/media/pictures/default-pfp/Otter1.png",
    "/media/pictures/default-pfp/Otter2.png",
    "/media/pictures/default-pfp/Otter3.png",
    "/media/pictures/default-pfp/Otter4.png",
    "/media/pictures/default-pfp/Otter5.png",
    "/media/pictures/default-pfp/Otter6.png",
    "/media/pictures/default-pfp/Otter7.png",
    "/media/pictures/default-pfp/Otter8.png",
    "/media/pictures/default-pfp/Otter9.png",
  ];

  const mapToFrontendEnum = (backendEnumValue) => {
    switch (backendEnumValue) {
      case "Beginner":
        return "BEGINNER";
      case "Intermediate":
        return "INTERMEDIATE";
      case "Advanced":
        return "ADVANCED";
      case "Expert":
        return "EXPERT";
      case "Professional":
        return "PROFESSIONAL";
      default:
        return backendEnumValue;
    }
  };

  useEffect(() => {
    if (user) {
      console.log("User logged in:", user);
      navigate("/");
    }
  }, [user, navigate]);

  const createProfile = async (event) => {
    event.preventDefault();
    const { userEmail, userPassword } = location.state;
    const newUser = {
      username,
      password: userPassword,
      email: userEmail,
      instrument,
      genre,
      experienceLevel: mapToFrontendEnum(experienceLevel), // Map to backend enum
      imageURL,
    };

    axiosBase.post("/api/users", newUser)
      .then(async () => {
        console.log("User created successfully, attempting to login...");
        await login(username, userPassword);
        console.log("Current user after login attempt:", user);
        navigate("/");
        setUsername("");
        setInstrument("");
        setGenre("");
        setExperienceLevel("");
        setImageURL("");
      })
      .catch(error => {
        console.error("Error creating user:", error.message);
        
      });
  };


  const handleNameChange = (event) => {
    setUsername(event.target.value);
  };
  const handleInstrumentChange = (event) => {
    setInstrument(event.target.value);
  };
  const handleGenreChange = (event) => {
    setGenre(event.target.value);
  };
  const handleExperienceChange = (event) => {
    setExperienceLevel(event.target.value);
  };
  const handlePictureChange = (event) => {
    setImageURL(event.target.value);
  };

  

  const renderDefaultProfilePics = () => {
    return (
      <div className="profile-pics-container">
        {defaultProfilePics.map((picUrl, index) => (
          <label key={index} className="profile-pic-option">
            <input
              type="radio"
              name="profilePic"
              value={picUrl}
              checked={imageURL === picUrl}
              onChange={handlePictureChange}
            />
            <img src={picUrl} alt={`Default profile ${index + 1}`} />
          </label>
        ))}
      </div>
    );
  };

  return (
    <div className="main-container">
      <h1>User Information</h1>
      <form onSubmit={createProfile}>
        <div className="profile-box">
          <label htmlFor="userNameInput">Username: </label>
          <input
            id="userNameInput"
            type="text"
            value={username}
            onChange={handleNameChange}
            placeholder="username"
          />
        </div>
        <div className="profile-box">
          <label htmlFor="instrumentInput">Instruments: </label>
          <input
            id="InstrumentInput"
            type="text"
            value={instrument}
            onChange={handleInstrumentChange}
            placeholder="Instruments"
          />
        </div>
        <div className="profile-box">
          <label htmlFor="genreInput">Genres: </label>
          <input
            id="genreInput"
            type="text"
            value={genre}
            onChange={handleGenreChange}
            placeholder="Rock, Classical, ect."
          />
        </div>
        <div className="profile-box">
          <label htmlFor="experienceLevel">Experience Level: </label>
          <select
  id="experienceInput"
  value={experienceLevel}
  onChange={handleExperienceChange}
>
 {["BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT", "PROFESSIONAL"].map((level) => (    
  <option key={level} value={level}>
      {level}
    </option>
  ))}
</select>
        </div>
        <div className="profile-box">
          <label htmlFor="imageUrl">Profile Picture: </label>
          <div>{renderDefaultProfilePics()}</div>
        </div>
        <div>
          <button type="submit" className="action-button">
            Finish Profile
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProfileCreation;
