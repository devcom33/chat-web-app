package com.example.demo.controller;


import com.example.demo.entities.Messages;
import com.example.demo.service.MessagesSevice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {
    private MessagesSevice messagesSevice;

    public ChatController(MessagesSevice messagesSevice){
        this.messagesSevice = messagesSevice;
    }

    @GetMapping(path="/user/chat")
    public List<Messages> chat(){
        return messagesSevice.listMessages();
    }



}
