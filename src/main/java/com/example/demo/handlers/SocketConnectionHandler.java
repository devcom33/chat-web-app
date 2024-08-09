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

public class SocketConnectionHandler extends TextWebSocketHandler {
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());
    Map<String, WebSocketSession> sessionMap = Collections.synchronizedMap(new HashMap<>());
    @Autowired
    MessagesSevice messagesSevice;
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

        messagesSevice.saveMessageToDatabase(senderUsername, messageDTO);

        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDTO)));
        }
        else{
            System.out.println(session.getId() + " disconnected");
        }
    }


}
