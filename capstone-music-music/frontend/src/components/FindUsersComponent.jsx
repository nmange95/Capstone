import React from "react";
import { Link } from "react-router-dom";
import "./FindUser.css"; // Import the CSS file

function FindUser({ results }) {
  return (
    <div>
      <h2>Search Results</h2>
      {Array.isArray(results) && results.length > 0 ? (
        <ul className="search-results">
          {results.map((result) => (
            <li key={result.id} className="search-result-item">
              <img
                src={result.imageURL || 'default-image-url.jpg'} // Fallback to a default image if imageURL is not available
                alt={`Profile of ${result.username}`}
                className="profile-image"
              />
              <div className="user-info">
                <Link to={`/user/${result.username}`}>
                  <p id="username">{result.username}</p>
                </Link>
                <p>Experience Level: {result.experienceLevel}</p>
                <p>Instrument: {result.instrument}</p>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p>No results found.</p>
      )}
    </div>
  );
}

export default FindUser;