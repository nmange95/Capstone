import React, { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import DisplayProfile from "../components/DisplayProfile";
import "./ProfilePage.css";
import EditProfileForm from "../components/EditProfileForm";


const ProfilePage = () => {
  const { user } = useAuth();
  const [editMode, setEditMode] = useState(false);

  const toggleEditMode = () => {
    setEditMode(!editMode);
  };

  if (!user) {
    console.log(user);
    return <div>Loading profile...</div>;
  }

  return (
    <div className="main-container display-profile-container">
      <div className="item-box">
        {editMode ? (
          <EditProfileForm user={user} toggleEditMode={toggleEditMode} />
        ) : (
          <>
            <DisplayProfile data={user} toggleEditMode={toggleEditMode} />
            
            
          </>
        )}
      </div>
    </div>
  );
};

export default ProfilePage;
