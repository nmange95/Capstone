import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';


const AudioHandler = ({ onFileChange }) => {
  const { user } = useAuth();
  const [audioFile, setAudioFile] = useState(null);

  // Function to handle file input change
  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    setAudioFile(file);

    if (onFileChange) {
      onFileChange(file);
    }
  };

  return (
    <div>
      <p><span style={{ fontWeight: 'bold' }}>User: </span>{user ? user.username : 'Not authenticated'}</p>
      <input type="file" accept="audio/*" onChange={handleFileChange} />
      {audioFile && (
        <div>
          <h3>Selected Audio</h3>
          <p>{audioFile.name}</p>
        </div>
      )}
    </div>
  );
};


export default AudioHandler;