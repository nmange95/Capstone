import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  

} from "react-router-dom";
import NewUser from "./pages/NewUser";
import ProfileCreation from "./pages/ProfileCreation";
import { AuthProvider, useAuth } from "./contexts/AuthContext";
import ProfilePage from "./pages/ProfilePage";
import Layout from "./components/Layout";
import LandingPage from "./pages/LandingPage";
import AudioPortfolio from "./pages/AudioPortfolio";
import AudioPlayer from "./components/AudioPlayer";
import AudioUpload from "./pages/AudioUpload";
import SignIn from "./pages/SignIn";
import FindUsers from "./pages/FindUsers";
import ViewUserProfile from "./pages/ViewUserProfile";
import { NotificationsProvider } from "./contexts/NotificationsContext";
import { ModalProvider } from "./contexts/ModalContext";
import FriendsPage from "./pages/FriendsPage";
import { Navigate } from "react-router-dom";

const AuthenticatedApp = () => {
  const { loading } = useAuth();

  return (
    <Layout>
      {loading ? (
        <p>Loading Sick Beats...</p>
      ) : (
          <Routes>
            <Route path="/" element={<ProfilePage />} />
            <Route path="/portfolio" element={<AudioPortfolio />} />
            <Route path="/portfolio/upload" element={<AudioUpload />} />
            <Route path="/friends" element={<FriendsPage />} />
            <Route path="/findUsers" element={<FindUsers />} />
            <Route path="/user/:username" element={<ViewUserProfile />} />
            <Route path="/audio/:id" element={<AudioPlayer />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      )}
    </Layout>
  );
};

const UnauthenticatedApp = () => {
  const { user, loading } = useAuth();

  return (
    <Routes>
      <Route path="/" element={<LandingPage user={user} loading={loading} />} />
      <Route path="/new-user" element={<NewUser />} />
      <Route path="/profile-creation" element={<ProfileCreation />} />
      <Route path="/sign-in" element={<SignIn />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

function App() {
  return (
    <AuthProvider>
      <NotificationsProvider>
        <ModalProvider>
          <Router>
            <MainApp />
          </Router>
        </ModalProvider>
      </NotificationsProvider>
    </AuthProvider>
  );
}

const MainApp = () => {
  const { user, loading } = useAuth();

  if(loading) {
    return <p>Loading...</p>;
  }

  return user ? <AuthenticatedApp /> : <UnauthenticatedApp />;
};

export default App;
