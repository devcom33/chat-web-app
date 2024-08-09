package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {

    private String recipientUsername;
    private String content;

    // Default constructor
    public MessageDTO() {
    }

    // Custom constructor with @JsonCreator and @JsonProperty annotations
    @JsonCreator
    public MessageDTO(
            @JsonProperty("recipientUsername") String recipientUsername,
            @JsonProperty("content") String content) {
        this.recipientUsername = recipientUsername;
        this.content = content;
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
}
