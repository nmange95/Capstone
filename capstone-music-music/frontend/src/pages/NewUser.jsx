import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./NewUser.css";
import { useAuth } from "../contexts/AuthContext";
import axiosBase from "../contexts/axiosBase";

const NewUser = ({ toggleLoginFunc }) => {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      console.log('User logged in:', user);
      navigate("/profile");
    }
  }, [user, navigate]);

  const handleEmailChange = (event) => {
    setUserEmail(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setUserPassword(event.target.value);
  };

  const checkUserExists = async (email) => {
    try {
      console.log(userEmail);
      const { status } = await axiosBase.get(`/api/users/search?email=${encodeURIComponent(email)}`);
      console.log(status);

      return status === 200;
    } catch (error) {
      console.error("Error checking user:", error.message);
      return error.response && error.response.status === 404 ? false : true;
    }
  };


  const handleUserInput = async (event) => {
    event.preventDefault();
    setUserEmail(event.target.elements.emailInput.value);
    setUserPassword(event.target.elements.passwordInput.value);

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(userEmail)) {
      alert("Must be a valid email address");
      return;
    }

    if (userPassword.length < 8) {
      alert("Password must be at least 8 characters long.");
      return;
    }

    const userExists = await checkUserExists(userEmail);
    if (!userExists) {
      navigate("/profile-creation", {
        state: { userEmail, userPassword },
      });
    } else {
      alert("Account already exists please log in.");
      return;
    }
  };

  return (
    <div className="signup">
      <h1 className="OtterCollab">Join OtterCollab</h1>
      <form onSubmit={handleUserInput}>
        <div className="email-box">
          <label htmlFor="emailInput">Email: </label>
          <input
            id="emailInput"
            type="email"
            name="email"
            value={userEmail}
            onChange={handleEmailChange}
            placeholder="Email"
          />
        </div>
        <div>
          <label htmlFor="passwordInput">Password: </label>
          <input
            id="passwordInput"
            type="password"
            value={userPassword}
            onChange={handlePasswordChange}
            placeholder="Password"
          />
        </div>
        <div>
          <button type="submit" className="action-button">
            Create Account
          </button>
          
        </div>
      </form>
      <p>
        Already on OtterCollab?{' '}
        <span className="create-account-link" onClick={toggleLoginFunc}>
          Sign in
        </span>
      </p>

    </div>
  );
};

export default NewUser;
