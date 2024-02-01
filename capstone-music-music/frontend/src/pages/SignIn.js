import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from "../contexts/AuthContext";
import './SignIn.css';

const SignIn = ({ toggleLoginFunc }) => {
  const { login } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (username && password) {
      try {
        await login(username, password);
        navigate("/");
      } catch (error) {
        alert('Failed to sign in');
        console.error(error);
      }
    } else {
      alert('Please enter both username and password!');
    }
  };

  return (
    <div className="SignIn">
      <h1 className="OtterCollab">OtterCollab</h1>
      <form onSubmit={handleSubmit}>
        <div className="username-box">
          <label htmlFor="usernameInput">Username: </label>
          <input
            id="usernameInput"
            type="text"
            value={username}
            onChange={handleUsernameChange}
            placeholder="Username"
          />
        </div>
        <div>
          <label htmlFor="passwordInput">Password: </label>
          <input
            id="passwordInput"
            type="password"
            value={password}
            onChange={handlePasswordChange}
            placeholder="Password"
          />
        </div>
        <div>
          <button type="submit" className="action-button">
            Sign In
          </button>
        </div>
      </form>
      <p>
        Not on OtterCollab?{' '}
        <span className="create-account-link" onClick={toggleLoginFunc}>
          Create Account
        </span>
      </p>
    </div>
  );
};

export default SignIn;