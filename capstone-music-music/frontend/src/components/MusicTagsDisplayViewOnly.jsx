import "../pages/ProfilePage.css";

const MusicTagsDisplayViewOnly = ({ data, onClickFunc }) => {

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
        <div className="music-tags-container">
            <div className="selected-tags">
                {data && data.map((tag) => (
                    <div key={tag} className="tag" onClick={onClickFunc}>
                        <span>{formatEnum(tag)}</span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MusicTagsDisplayViewOnly;
