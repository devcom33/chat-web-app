package com.example.demo.handlers;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.service.MessagesSevice;
import com.example.demo.sec.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SocketConnectionHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    MessagesSevice messagesSevice;

    @Autowired
    private JwtUtil jwtUtil;

    public SocketConnectionHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String token = getTokenFromSession(session);
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            sessionMap.put(username, session);
            System.out.println(username + " connected");
        } else {
            System.out.println(session.getId() + " connected with an invalid or missing token");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
        }
    }

    private String getTokenFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().getQueryParams().toSingleValueMap();
        return queryParams.get("token");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println(session.getId() + " disconnected");
        sessionMap.values().remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String payload = message.getPayload();

        MessageDTO messageDTO = objectMapper.readValue(payload, MessageDTO.class);
        String senderUsername = jwtUtil.getUsernameFromToken(getTokenFromSession(session));
        WebSocketSession recipientSession = sessionMap.get(messageDTO.getRecipientUsername());

        // Save the message to the database
        //messagesSevice.saveMessageToDatabase(senderUsername, messageDTO);

        if (recipientSession != null && recipientSession.isOpen()) {

            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDTO)));
        }

        // Log the message type
        System.out.println("Received message of type: " + messageDTO.getType());
    }
}
