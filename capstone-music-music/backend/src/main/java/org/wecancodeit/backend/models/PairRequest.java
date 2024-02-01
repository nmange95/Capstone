package org.wecancodeit.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "pair_requests")
public class PairRequest {

    // To determine the states a request can be in
    public enum RequestStatus {
        PENDING, ACCEPTED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private LocalDateTime sentDateTime;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    // message that goes with pair requests
    private String message;

    public PairRequest() {
        this.sentDateTime = LocalDateTime.now();
        this.requestStatus = RequestStatus.PENDING;

    }

    public PairRequest(User sender, User receiver, LocalDateTime sentDateTime, RequestStatus requestStatus,
            String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.sentDateTime = sentDateTime;
        this.requestStatus = requestStatus;
        this.message = message;

    }

    public long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getSentDateTime() {
        return sentDateTime;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PairRequest{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ",sentDateTime=" + sentDateTime +
                ", requestStatus=" + requestStatus +
                ", message=" + message + '\'' + '}';
    }

}
