import { useAuth } from "../contexts/AuthContext";
import MusicTagsDropdown from "../components/MusicTagsDropdown";
import { Link } from "react-router-dom";


function DisplayProfile({ toggleEditMode }) {
  const { user, loadProfileData } = useAuth();

  const formatEnum = (input) => {
    if (input) {
      console.log(input)
      return input.split('_')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(' ');
    } else {
      return '';
    }
  }

  return (
    <div className="display-profile-container">
      <div className="profile-details-section">
        <div className="profile-header">
          <img src={user.imageURL} alt="Profile" className="profile-picture" />
          <h1 className="profile-username">{user.username}</h1>
        </div>
        <div className="profile-info">
          <h2>Email:</h2>
          <p>{user.email}</p>
        </div>
        <div className="profile-info">
          <h2>Instrument:</h2>
          <p>{user.instrument}</p>
        </div>
        <div className="profile-info">
          <h2>Experience:</h2>
          <p>{formatEnum(user.experienceLevel)}</p>
        </div>
        <div className="profile-info">
          <h2>Genre:</h2>
          <p>{user.genre}</p>
        </div>
      </div>
      <button onClick={toggleEditMode}>Edit Profile</button>
      <MusicTagsDropdown user={user} loadProfileData={loadProfileData} />
      <button> <Link to="/portfolio" style={{textDecoration: "none"}}>View Your Audio Uploads Portfolio</Link></button>
    </div>
  );
}



export default DisplayProfile;
