import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from "react-router-dom";
import AudioHandler from "../components/AudioHandler";

function AudioUpload() {
    const { user } = useAuth();
    const [file, setFile] = useState(null);
    const [title, setTitle] = useState('');
    const [artist, setArtist] = useState('');
    const [genre, setGenre] = useState('');
    const navigate = useNavigate();

    const handleSuccessfulUpload = () => {
        // Reset form fields or add any other logic needed after successful upload
        setFile(null);
        setTitle('');
        setArtist('');
        setGenre('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!user) {
            console.error('User not authenticated');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('title', title);
        formData.append('artist', artist);
        formData.append('genre', genre);
        formData.append('userId', user.id);

        try {
            const response = await fetch('http://localhost:8080/api/audio/upload', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${user.token}`
                },
                body: formData
            });

            if (response.ok) {
                const responseData = await response.json();
                console.log('Response:', responseData);
                handleSuccessfulUpload();
                navigate("/portfolio");
            } else {
                console.error('Upload error: Server responded with a non-2xx status code');
                // Check if the response body is not empty before parsing as JSON
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    const errorData = await response.json();
                    console.error('Error Details:', errorData.message);
                } else {
                    console.error('Error Details: No additional information');
                }
            }
        } catch (error) {
            console.error('Upload error:', error.message);
        }
    };



    return (
        <div className="main-container">
            <h2>Upload Audio</h2>
            <AudioHandler onFileChange={(file) => setFile(file)} />
            <form onSubmit={handleSubmit}>
                <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} placeholder="Title" />
                <input type="text" value={artist} onChange={(e) => setArtist(e.target.value)} placeholder="Artist" />
                <input type="text" value={genre} onChange={(e) => setGenre(e.target.value)} placeholder="Genre" />
                <button type="submit">Upload</button>
            </form>
            {/* Add an audio player for playback */}
            {file && (
                <div>
                    <h3>Selected Audio</h3>
                    <p>{file.name}</p>
                    <audio controls>
                        <source src={URL.createObjectURL(file)} type="audio/mp3" />
                        Your browser does not support the audio tag.
                    </audio>
                </div>
            )}
        </div>
    );
}
export default AudioUpload;
