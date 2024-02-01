import { useNotifications } from "../contexts/NotificationsContext.js";
import { useModal } from "../contexts/ModalContext";
import axiosBase from "../contexts/axiosBase";

const SendPairRequest = ({ senderUser, receiverUser, onSendSuccess }) => {
  const { hideModal } = useModal();
  const { addNotification } = useNotifications();

  const handleSendPairRequest = async () => {
    try {
      const pairRequestData = {
        sender: senderUser,
        receiver: receiverUser,
        message: "Let's collaborate!"
      };

      const response = await axiosBase.post("/api/pair-requests/send", pairRequestData);

      if (response.status === 200) {
        console.log("Pair request sent successfully");

        hideModal();
        if (onSendSuccess) {
          onSendSuccess();
        }
      } else {
        console.error("Failed to send pair request: ", response.status);
      }
    } catch (error) {
      console.error("Error in sending pair request: ", error);
    }
  };

  return (
    <div>
      <div className="modal-backdrop">
        <div className="modal-content">
          <h2>Request to Pair?</h2>
          <p>Do you want to send this artist a request to pair?</p>
          <div className="modal-content">
            <button onClick={handleSendPairRequest}>Send Request</button>
            <button onClick={hideModal}>Cancel</button>
          </div>
        </div>
      </div>
    </div>
  );
};


export default SendPairRequest;
