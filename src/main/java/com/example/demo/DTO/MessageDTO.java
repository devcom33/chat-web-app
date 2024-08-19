package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {

    private String recipientUsername;
    private String content;
    private String sender; // Add this field

    // Default constructor
    public MessageDTO() {
    }

    // Custom constructor with @JsonCreator and @JsonProperty annotations
    @JsonCreator
    public MessageDTO(
            @JsonProperty("recipientUsername") String recipientUsername,
            @JsonProperty("content") String content,
            @JsonProperty("sender") String sender) { // Include sender in constructor
        this.recipientUsername = recipientUsername;
        this.content = content;
        this.sender = sender;
    }

    // Getters and setters
    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
