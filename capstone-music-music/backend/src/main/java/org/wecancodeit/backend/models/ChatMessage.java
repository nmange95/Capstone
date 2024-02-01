package org.wecancodeit.backend.models;


import java.util.Date;
import jakarta.persistence.*;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sender;
    private String recipient;
    private String text;
    private Date timestamp;

    
    public ChatMessage() {
    }

    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String from) {
        this.sender = from;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    
}
