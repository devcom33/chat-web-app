package com.example.demo.service;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.entities.AppUser;
import com.example.demo.entities.Chat;
import com.example.demo.entities.Messages;
import com.example.demo.repo.AppUserRepository;
import com.example.demo.repo.ChatRepository;
import com.example.demo.repo.MessagesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
public class MessagesSeviceImpl implements MessagesSevice {
    @Autowired
    ChatRepository chatRepositoy;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    MessagesRepository messagesRepository;

    @Override
    public void saveMessageToDatabase(String senderUsername, MessageDTO messageDTO) {
        AppUser sender = appUserRepository.findByUsername(senderUsername);
        AppUser recipient = appUserRepository.findByUsername(messageDTO.getRecipientUsername());

        Chat chat = chatRepositoy.findByMembersContains(sender)
                .orElseGet(() -> {
                    Chat newChat = new Chat("Chat Between " + sender.getUsername() + " and " + recipient.getUsername(),
                            LocalDateTime.now(), LocalDateTime.now(),
                            Arrays.asList(sender, recipient));
                    chatRepositoy.save(newChat);  // Save the chat before using it
                    return newChat;
                });

        Messages message = new Messages();
        message.setContent(messageDTO.getContent());
        message.setSender(sender);
        message.setChat(chat);
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);

        messagesRepository.save(message);
    }
}
