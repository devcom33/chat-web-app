package com.example.demo.handlers;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.sec.entities.AppUser;
import com.example.demo.sec.entities.Chat;
import com.example.demo.sec.entities.Messages;
import com.example.demo.sec.repo.AppUserRepository;
import com.example.demo.sec.repo.ChatRepository;
import com.example.demo.sec.repo.MessagesRepository;
import com.example.demo.sec.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

public class SocketConnectionHandler extends TextWebSocketHandler {
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());
    Map<String, WebSocketSession> sessionMap = Collections.synchronizedMap(new HashMap<>());
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    ChatRepository chatRepositoy;
    @Autowired
    MessagesRepository messagesRepository;
    String username, tok;
    @Autowired
    private JwtUtil jwtUtil;

    public SocketConnectionHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        tok = getTokenFromSession(session);
        if (tok != null && jwtUtil.validateToken(tok)) {
            username = jwtUtil.getUsernameFromToken(tok);
            System.out.println(username + " connected");
        } else {
            System.out.println(session.getId() + " connected without a valid token");
        }
        webSocketSessions.add(session);
        sessionMap.put(username, session);
    }

    private String getTokenFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().getQueryParams().toSingleValueMap();
        return queryParams.get("token");
    }
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println(session.getId() + " disconnected");
        webSocketSessions.remove(session);
        sessionMap.values().remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String payload = message.getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO messageDTO = objectMapper.readValue(payload, MessageDTO.class);
        String senderUsername = jwtUtil.getUsernameFromToken(getTokenFromSession(session));
        WebSocketSession recipientSession = sessionMap.get(messageDTO.getRecipientUsername());

        saveMessageToDatabase(senderUsername, messageDTO);

        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDTO)));
        }
        else{
            System.out.println(session.getId() + " disconnected");
        }
    }
    private void saveMessageToDatabase(String senderUsername, MessageDTO messageDTO) {
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
