// Notification.java

package org.wecancodeit.backend.models;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    private LocalDateTime timestamp;
    private boolean seen;

    @ManyToOne
    private PairRequest request;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Notification() {
        this.timestamp = LocalDateTime.now();
    }

    public Notification(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.request = null; // No pair request associated
    }

    public Notification(String message, PairRequest request) {
        this.message = message;
        this.request = request;
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public PairRequest getRequest() {
        return request;
    }

    public void setRequest(PairRequest request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
