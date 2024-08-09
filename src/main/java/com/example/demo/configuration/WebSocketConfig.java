package com.example.demo.configuration;

import com.example.demo.handlers.SocketConnectionHandler;
import com.example.demo.sec.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {
    private final JwtUtil jwtUtil;

    public WebSocketConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketConnectionHandler(), "/hello")
                .setAllowedOrigins("*"); // Adjust as needed
    }

    @Bean
    public SocketConnectionHandler socketConnectionHandler() {
        return new SocketConnectionHandler(jwtUtil);
    }
}
