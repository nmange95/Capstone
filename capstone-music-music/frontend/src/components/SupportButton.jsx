// SupportButton.jsx
import React from 'react';
import { useModal } from '../contexts/ModalContext';
import SupportFeedbackForm from './SupportFeedbackForm';

const SupportButton = () => {
  const { showModal } = useModal();

  const openSupportModal = () => {
    showModal(<SupportFeedbackForm onClose={() => showModal(null)} />);
  };

  return (
    <div className="support-button-collapsed">
    <div onClick={openSupportModal}>
      Support
    </div>
    </div>
  );
};

export default SupportButton;
