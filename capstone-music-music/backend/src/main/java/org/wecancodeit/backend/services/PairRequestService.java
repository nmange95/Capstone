package org.wecancodeit.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.wecancodeit.backend.models.Notification;
import org.wecancodeit.backend.models.PairRequest;
import org.wecancodeit.backend.models.PairRequest.RequestStatus;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.repositories.PairRequestRepository;
import org.wecancodeit.backend.repositories.UserRepository;

@Service
public class PairRequestService {

    @Autowired
    private PairRequestRepository pairRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public PairRequest savePairRequest(PairRequest request) {
        User sender = userRepository.findByUsername(request.getSender().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findByUsername(request.getReceiver().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // Check for duplication
        List<PairRequest> existingRequests = pairRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (!existingRequests.isEmpty()) {
            return null;
        }

        request.setSender(sender);
        request.setReceiver(receiver);
        request.setRequestStatus(RequestStatus.PENDING);

        // Save the pair request
        PairRequest savedRequest = pairRequestRepository.save(request);

        if (savedRequest != null) {
            Notification notification = new Notification();
            notification.setMessage("You have a new pair request from " + savedRequest.getSender().getUsername());
            notification.setUser(savedRequest.getReceiver());
            notification.setRequest(savedRequest);
            notificationService.saveNotification(notification);
        }

        if (savedRequest != null) {
            String recipientUsername = savedRequest.getReceiver().getUsername();
            String notificationMsg = "UPDATE";
            messagingTemplate.convertAndSendToUser(recipientUsername, "/queue/notifications", notificationMsg);
        }
        return savedRequest;
    }

    public boolean requestExistsBySenderAndReceiver(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        List<PairRequest> existingRequests = pairRequestRepository.findBySenderAndReceiver(sender, receiver);
        return !existingRequests.isEmpty();
    }

    // Service method to handle different states of a pair request
    public List<PairRequest> getPairRequestsByStatus(RequestStatus status) {
        // Implementation to retrieve pair requests by their status from the database
        return pairRequestRepository.findByRequestStatus(status);
    }

    public List<PairRequest> getPendingPairRequests(String username) {
        // Retrieve a user from the database using the provided username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Pending pair requests for the user from the database
        return pairRequestRepository.findByReceiverAndRequestStatus(user, RequestStatus.PENDING);
    }

    public void respondToPairRequest(@NonNull Long pairRequestId, RequestStatus response) {
        PairRequest pairRequest = pairRequestRepository.findById(pairRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Pair Request not found"));

        pairRequest.setRequestStatus(response);

        if (response == RequestStatus.ACCEPTED) {
            User sender = pairRequest.getSender();
            User receiver = pairRequest.getReceiver();

            if (sender != null && receiver != null) {
                boolean friendsSet = userService.setUserFriends(sender, receiver);
                if (friendsSet) {
                    String notificationMsg = "UPDATE";

                    String username1 = sender.getUsername();
                    messagingTemplate.convertAndSendToUser(username1, "/queue/notifications", notificationMsg);

                    String username2 = receiver.getUsername();
                    messagingTemplate.convertAndSendToUser(username2, "/queue/notifications", notificationMsg);
                }
            }
        }

        pairRequestRepository.save(pairRequest);
    }
}
