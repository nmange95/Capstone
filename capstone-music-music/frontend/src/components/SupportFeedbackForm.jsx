import React, { useState } from 'react';

const SupportFeedbackForm = ({ onClose }) => {
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
  
    // Prevent multiple submissions
    if (submitted) {
      return;
    }
  
    // Validate form fields
    const email = document.getElementById('email').value.trim();
    const helpInput = document.getElementById('helpInput').value.trim();
  
    if (!email || !helpInput) {
      // Display an error message or take appropriate action
      alert('Please fill in the Email and "How can we help you?" fields before submitting.');
      return;
    }
  
    // Show the message
    setSubmitted(true);
  };
  
  

  return (
    <div className='submit-form'>
      <h2>Support & Feedback</h2>
      {!submitted && <form onSubmit={handleSubmit}>
        <label htmlFor="user">Username</label>
        <input id="user" type="text" disabled={submitted} />
        <label htmlFor="email">Email Address</label>
        <input id="email" type="email" disabled={submitted} />
        <label htmlFor="helpInput" className='support-input'>
          How can we help you?
          <textarea id="helpInput" rows="4" cols="50" resize="none" className='support-input' disabled={submitted}></textarea>
        </label>
        <br />
        <br />
        <input type="submit" className={`submit-support-button ${submitted ? 'disabled' : ''}`} disabled={submitted} />
      </form>}

      {submitted && (
        <p className='submitted-message'>Thank you for your feedback! Someone from our team will 
        review your submission and reach out to you shortly.</p>
      )}

      <br />
      <button onClick={onClose} className='support-close'>Close</button>
    </div>
  );
};

export default SupportFeedbackForm;
