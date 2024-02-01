package org.wecancodeit.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.wecancodeit.backend.models.PairRequest;
import org.wecancodeit.backend.models.PairRequest.RequestStatus;
import org.wecancodeit.backend.models.User;

@Repository
// Queries
public interface PairRequestRepository extends JpaRepository<PairRequest, Long> {

    List<PairRequest> findByReceiverAndRequestStatus(User receiver, PairRequest.RequestStatus requestStatus);

    List<PairRequest> findByRequestStatus(RequestStatus requestStatus);

    List<PairRequest> findBySender_Username(String senderUsername);

    List<PairRequest> findByReceiver_Username(String receiverUsername);
    
    List<PairRequest> findBySenderAndReceiver(User sender, User receiver);
}
