import React, { createContext, useState, useEffect, useContext } from "react";
import axiosBase from "./axiosBase";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [profileLoaded, setProfileLoaded] = useState(false);

  useEffect(() => {
    const validateToken = async () => {
      const storedToken = localStorage.getItem("authToken");
      console.log("Retrieved token from localStorage:", storedToken);

      if (storedToken) {
        try {
          const response = await axiosBase.get("/api/auth/checkStatus", {
            headers: {
              Authorization: `Bearer ${storedToken}`,
            },
          });

          const username = response.data;
          setUser({ token: storedToken, username });
          await loadProfileData(username);
          setProfileLoaded(true);
        } catch (error) {
          console.error("Error validating token:", error);
          localStorage.removeItem("authToken");
          setUser(null);
        }
      }
      console.log("current user:", user);
    };

    validateToken();
  }, []);

  useEffect(() => {
    console.log("profileLoaded updated to: ", profileLoaded);
  }, [profileLoaded]);

  const loadProfileData = async (username) => {
    if (username === null || username === undefined) {
      username = user.username;
    }

    axiosBase
      .get(`/api/users/search?username=${username}`)
      .then((response) => {
        setUser((current) => ({ ...current, ...response.data }));
        setProfileLoaded(true);
      })
      .catch((error) => console.error("Error fetching profile data:", error));
  };

  const login = async (username, password) => {
    axiosBase
      .post("/api/auth/login", { username, password })
      .then((response) => {
        localStorage.setItem("authToken", response.data.token);
        setUser({ token: response.data.token, username: username });
        loadProfileData(username);
        console.log("successfully logged in");
      })
      .catch((error) => console.error("An error occurred:", error));
  };

  const logout = () => {
    localStorage.removeItem("authToken");
    setUser(null);
  };

  const updateUsername = async (newName) => {
    setUser((prevUser) => {
        const updatedUser = {...prevUser, username: newName};
        login(newName, prevUser.password);
        return updatedUser;
    });

  };

  return (
    <AuthContext.Provider
      value={{ user, login, logout, loadProfileData, updateUsername }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
