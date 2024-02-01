import React from "react";
import { Link, useLocation } from "react-router-dom";
import "./Navbar.css";
import { useAuth } from "../contexts/AuthContext.js";
import { useModal } from "../contexts/ModalContext";
import LogoutModal from "./LogOutModal";
import NotificationsMenu from "./NotificationsMenu";

function Navbar() {
  const { user } = useAuth();
  const { showModal } = useModal();
  const location = useLocation();

  const handleLogoutClick = (event) => {
    event.preventDefault();
    showModal(<LogoutModal />);
  };

  

  return (
    <nav className="navbar">
      <ul className="nav-links">
        <div className="Logo-Home-Button">
          <li>
            <Link className="logo-home-button" to="/">
              <img
                src="/media/pictures/logo.png"
                alt="LOGO OTTERCOLLAB"
                className="logo"
              ></img>
              <img
                src="/media/pictures/hover-logo.png"
                alt="LOGO OTTERCOLLAB TRACED IN WHITE"
                className="hover-logo"
              />
            </Link>
          </li>
        </div>
        <div></div> {/* spacer for nav */}
        <div className="nav-links">
          <div className={location.pathname === "/" ? "active" : ""}>
            <li>
              <Link to="/">Profile</Link>
            </li>
          </div>
          <div className={location.pathname === "/portfolio" ? "active" : ""}>
            <li>
              <Link to="/portfolio">Portfolio</Link>
            </li>
          </div>
          <div className={location.pathname === "/friends" ? "active" : ""}>
            <li>
              <Link to="/friends">Friends</Link>
            </li>
          </div>
          <div className={location.pathname === "/findUsers" ? "active" : ""}>
            <li>
              <Link to="/findUsers">Find Users</Link>
            </li>
          </div>
        </div>
        <div className="notification-menu">
          {user && (
            <li>
              <NotificationsMenu />
            </li>
          )}
        </div>
        <div className="logout-button-and-modal">
          <li>
            <button onClick={handleLogoutClick} className="link-button">
              Log Out
            </button>
          </li>
        </div>
      </ul>
    </nav>
  );
}

export default Navbar;
