package com.example.demo.service;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.entities.Messages;

import java.util.List;

public interface MessagesSevice {
    void saveMessageToDatabase(String senderUsername, MessageDTO messageDTO);
    List<Messages> listMessages();
}
