import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext.js';
import { useNavigate } from 'react-router-dom';
import SignIn from './SignIn';
import NewUser from './NewUser';

const LandingPage = () => {
  const { user, loading } = useAuth();
  const navigate = useNavigate();
  const [showCreateForm, setShowCreateForm] = useState(false);

  useEffect(() => {
    // Redirect to the profile page after 3 seconds if the user is authenticated
    if (!loading && user) {
      const redirectTimeout = setTimeout(() => {
        navigate.push('/profile');
      }, 3000);

      return () => clearTimeout(redirectTimeout);
    }
  }, [loading, user, navigate]);

  const toggleLogin = () => {
    setShowCreateForm(!showCreateForm);
  }

  return (
    loading ? (
      <p>Loading Sick Beats...</p>
    ) : (
      user ? (
        // User is authenticated, show welcome message
        <div className="welcome-back">
          <h1>Welcome back to OtterCollabs, {user.username}!</h1>
          {user.imageURL && (
            <img src={user.imageURL} alt={`${user.username}'s profile`} />
          )}
          {/* Redirect to the profile page after 3 seconds */}
        </div>
      ) : (
        // User is not authenticated, show landing page content
        <div className="main-container landing-page-container">
          <h1>Discover the Power of Collaborative Music Creation</h1>
          {!showCreateForm ? (
            <SignIn toggleLoginFunc={toggleLogin} />
          ) : (
            <NewUser toggleLoginFunc={toggleLogin} />
          )}
          <p className="landing-description">
           <h2> Welcome to <strong>OtterCollab</strong></h2> The nexus for rock stars and rising stars alike. Here, creativity knows no bounds, and every connection is a potential masterpiece in the making. With OtterCollab, artists from every corner of the globe come together to compose, share, and produce music in real-time.Begin your journey with OtterCollab and transform the way you create music. <h3><strong>Join and unlock the power of global collaboration today!</strong></h3>
          </p>
        </div>
      )
    )
  );
};

export default LandingPage;