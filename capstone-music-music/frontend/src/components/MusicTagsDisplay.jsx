import "../pages/ProfilePage.css";

const MusicTagsDisplay = ({ user, handleTagDelete }) => {

  const formatEnum = (input) => {
    if (input) {
      return input.split('_')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(' ');
    } else {
      return '';
    }
  }

  return (

    <div className="selected-tags">
      {user && user.musicTags && user.musicTags.map((tag) => (
        <div key={tag} className="tag">
          <span>{formatEnum(tag)}</span>
          <button onClick={() => handleTagDelete(tag)}>&times;</button>
        </div>
      ))}
    </div>
  );
};

export default MusicTagsDisplay;
