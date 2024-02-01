import React, { createContext, useContext, useState, useCallback } from 'react';

// Step 1: Create the Context
const ModalContext = createContext({
    showModal: (component) => { },
    hideModal: () => { },
});

// Step 2: Create a Custom Hook
export const useModal = () => useContext(ModalContext);

// Step 3: Implement the Provider
export const ModalProvider = ({ children }) => {
    const [modalContent, setModalContent] = useState(null);

    const showModal = useCallback((component) => {
        setModalContent(component);
    }, []);

    const hideModal = useCallback(() => {
        setModalContent(null);
    }, []);

    return (
        <ModalContext.Provider value={{ showModal, hideModal }}>
            {children}
            {modalContent && (
                <div className="modal-backdrop">
                    <div className="modal-content">
                        {modalContent}
                    </div>
                </div>
            )}
        </ModalContext.Provider>
    );
};
