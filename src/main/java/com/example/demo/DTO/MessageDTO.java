package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {

    private String recipientUsername;
    private String content;
    private String sender;
    private String type;


    // Default constructor
    public MessageDTO() {
    }

    // Custom constructor with @JsonCreator and @JsonProperty annotations
    @JsonCreator
    public MessageDTO(
            @JsonProperty("recipientUsername") String recipientUsername,
            @JsonProperty("content") String content,
            @JsonProperty("sender") String sender,
            @JsonProperty("type") String type) {
        this.recipientUsername = recipientUsername;
        this.content = content;
        this.sender = sender;
        this.type = type;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
