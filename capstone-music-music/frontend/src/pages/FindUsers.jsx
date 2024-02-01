import { useAuth } from "../contexts/AuthContext";
import React, { useState } from "react";
import FindUser from "../components/FindUsersComponent";
import axiosBase from "../contexts/axiosBase";

const FindUsers = () => {
  const { user } = useAuth();

  const [searchCriteria, setSearchCriteria] = useState({
    userSearching: user.username,
    username: "",
    email: "",
    instrument: "",
  });
  const [showResults, setShowResults] = useState(false);
  const [results, setResults] = useState(null);

  const handleInputChange = (e) => {
    setSearchCriteria({ ...searchCriteria, [e.target.name]: e.target.value });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    console.log("Search Criteria:", searchCriteria);
    await searchUsers();
    setShowResults(true);
  };

  const searchUsers = () => {
    axiosBase
      .post("/api/users/search-all", searchCriteria, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => setResults(response.data))
      .catch((error) => {
        setResults(null);
        console.error("Error fetching search results:", error);
      });
  };

  return (
    <div className="main-container">
      <form onSubmit={handleSearch}>
        <div>
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            name="username"
            value={searchCriteria.username}
            onChange={handleInputChange}
          />
        </div>
        <div>
          <label htmlFor="email">Email:</label>
          <input
            type="text"
            name="email"
            value={searchCriteria.email}
            onChange={handleInputChange}
          />
        </div>
        <div>
          <label htmlFor="instrument">Instrument:</label>
          <input
            type="text"
            name="instrument"
            value={searchCriteria.instrument}
            onChange={handleInputChange}
          />
        </div>
        <button type="submit">Search</button>
      </form>
      <div>{showResults && <FindUser results={results} />}</div>
    </div>
  );
};

export default FindUsers;
