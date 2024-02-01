import React, { useState } from 'react';


// implement by importing and then using <AddFriendModal></AddFriendModal>

const AddFriendModal = () => {
  const [showModal, setShowModal] = useState(false);

  const openModal = () => {
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const handleAddFriend = async () => {
    try {
      await console.log('Friend request sent'); 
      closeModal();
    }catch (error) {
    console.error('Failed to send friend request:', error);
    }
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
