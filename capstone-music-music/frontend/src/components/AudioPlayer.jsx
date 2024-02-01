import React, { useEffect, useState, useRef } from 'react';

const AudioPlayer = ({ audioId }) => {
  const [audioUrl, setAudioUrl] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [volume, setVolume] = useState(0.6);
  const audioRef = useRef(new Audio());

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/api/audio/stream/${audioId}`);
        if (!response.ok) {
          throw new Error('Network response was not ok: ' + response.status);
        }
        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        setAudioUrl(url);
      } catch (error) {
        console.error('Error fetching audio:', error);
        setError('Error fetching audio. Please try again');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [audioId]);

  useEffect(() => {
    const audio = audioRef.current;

    const handleTimeUpdate = () => {
      setCurrentTime(audio.currentTime);
      console.log("setCurrentTime ran");
    };

    if (audioUrl) {
      audio.src = audioUrl;
      audio.type = 'audio/mpeg';
      audio.volume = volume;
      audio.load();

      audio.addEventListener('timeupdate', handleTimeUpdate);

      audio.onloadedmetadata = () => {
        setCurrentTime(audio.currentTime);
      };
    }

    return () => {
      if (audio) {
        audio.removeEventListener('timeupdate', handleTimeUpdate);
        audio.pause();
        audio.src = '';
        URL.revokeObjectURL(audioUrl);
      }
    };
  }, [audioUrl, volume]);

  const toggleAudio = () => {
    const audio = audioRef.current;
    if (audio.paused) {
      audio.play().catch(error => {
        console.error('Error playing audio:', error);
        setError('Playback error. Please try again.');
      });
    } else {
      audio.pause();
    }
    setIsPlaying(!audio.paused);
  };

  return (
    <div className='music-player'>
      {error ? (
        <p>{error}</p>
      ) : loading ? (
        <p>Loading audio...</p>
      ) : (
        <>
          <button onClick={toggleAudio}>{isPlaying ? 'Pause' : 'Play Audio'}</button>
          <input
            className='volume-bar'
            type="range"
            min="0"
            max="1"
            step="0.1"
            value={volume}
            onChange={(e) => setVolume(parseFloat(e.target.value))}
          />
          <input
            className='time-scroll'
            type="range"
            min="0"
            max={audioRef.current.duration || 0}
            step="0.1"
            value={currentTime}
            onChange={(e) => {
              const newTime = parseFloat(e.target.value);
              setCurrentTime(newTime);
              audioRef.current.currentTime = newTime;
            }}
          />
        </>
      )}
    </div>
  );
};

export default AudioPlayer;
