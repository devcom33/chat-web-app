package com.example.demo.service;

import com.example.demo.DTO.MessageDTO;

public interface MessagesSevice {
    void saveMessageToDatabase(String senderUsername, MessageDTO messageDTO);
}
