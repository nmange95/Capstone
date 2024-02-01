// AboutFooterText.js
import React from 'react';

const AboutFooterText = () => {
  const openReadmeInNewTab = async () => {
    try {
      const readmeUrl = '/media/README.html';

      const response = await fetch(readmeUrl);
      const readmeContent = await response.text();

      const newTab = window.open();
      if (!newTab) {
        console.error('Failed to open a new tab');
        return;
      }

      newTab.document.write('<!DOCTYPE html><html lang="en"><head>');
      newTab.document.write('<meta charset="UTF-8">');
      newTab.document.write('<meta name="viewport" content="width=device-width, initial-scale=1.0">');
      newTab.document.write('</head><body>');

      const iframe = newTab.document.createElement('iframe');
      iframe.style.width = '100%';
      iframe.style.height = '100vh';
      iframe.style.border = 'none';
      iframe.srcdoc = readmeContent;
      newTab.document.body.appendChild(iframe);

      newTab.document.write('</body></html>');
    } catch (error) {
      console.error('Error opening README:', error);
    }
  };

  return (
    <div>
      <p className='footer'>
        <a href='#' onClick={openReadmeInNewTab} style={{ textDecoration: 'none' }}>
          &copy; OtterCollab 2024
        </a>
      </p>
    </div>
  );
};

export default AboutFooterText;
