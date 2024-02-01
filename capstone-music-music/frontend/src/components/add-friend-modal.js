import React, { useState } from 'react';
// import backend content?

// implement using <div><AddFriendModal></AddFriendModal></div>

const AddFriendModal = () => {
  const [showModal, setShowModal] = useState(false);

  const openModal = () => {
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const handleAddFriend = () => {
    // Perform actions when Add Friend is clicked
    console.log('Friend request sent');
    closeModal();
  };

  return (
    <div>
      <button onClick={openModal}>Add Friend</button>
      {showModal && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <h2>Add Friend</h2>
            <p>Do you want to add this user as a friend?</p>
            <div className="btn-container">
              <button onClick={handleAddFriend}>Add Friend</button>
              <button onClick={closeModal}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AddFriendModal;
