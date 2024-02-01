package org.wecancodeit.backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.wecancodeit.backend.models.PairRequest;
import org.wecancodeit.backend.models.PairRequest.RequestStatus;
import org.wecancodeit.backend.services.PairRequestService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/pair-requests")
public class PairRequestController {

    @Autowired
    private PairRequestService pairRequestService;

    // Sending a pair request
    @PostMapping("/send")
    public ResponseEntity<?> sendPairRequest(@RequestBody PairRequest request) {
        PairRequest createdRequest = pairRequestService.savePairRequest(request);
        return ResponseEntity.ok(Map.of("pairRequestId", createdRequest.getId()));
    }

    // Fetch Pair Requests by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PairRequest>> getPairRequestsByStatus(@PathVariable RequestStatus status) {
        List<PairRequest> pairRequests = pairRequestService.getPairRequestsByStatus(status);
        return ResponseEntity.ok(pairRequests);
    }

    // Fetch Pending requests for the user
    @GetMapping("/pending/{username}")
    public ResponseEntity<List<PairRequest>> getPendingPairRequests(@PathVariable String username) {
        List<PairRequest> pendingPairRequests = pairRequestService.getPendingPairRequests(username);
        return ResponseEntity.ok(pendingPairRequests);
    }

    // Handle Response to a request
    @PostMapping("/respond")
    public ResponseEntity<Void> respondToPairRequest(@RequestParam @NonNull Long pairRequestId,
            @RequestParam RequestStatus response) {
        pairRequestService.respondToPairRequest(pairRequestId, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending/{username1}/{username2}")
    public ResponseEntity<?> requestExistsBySenderAndReceiver(@PathVariable String username1, @PathVariable String username2) {
        boolean exists = pairRequestService.requestExistsBySenderAndReceiver(username1, username2);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
