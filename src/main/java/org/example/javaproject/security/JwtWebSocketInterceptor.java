package org.example.javaproject.security;

import org.example.javaproject.util.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// Beveiliging van de websocket
// Validatie van de JWT voor de handshake
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/socket/server/HandshakeInterceptor.html
@Component
public class JwtWebSocketInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public JwtWebSocketInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        // Cast naar ServletServerHttpRequest om toegang te krijgen tot de query parameters
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("token");


            if (token != null && !token.isEmpty()) {
                try {
                    // Valideer token
                    if (jwtUtil.validateToken(token)) {
                        String username = jwtUtil.extractUsername(token);
                        // Slaag de user op in de attributes map
                        // Die is nu beschikbaar gedurende de volledige Websocket
                        attributes.put("username", username);
                        System.out.println("WebSocket handshake successful for user: " + username);
                        return true;
                    } else {
                        System.out.println("WebSocket handshake failed: Invalid JWT token");
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("WebSocket handshake failed: JWT validation error - " + e.getMessage());
                    return false;
                }
            } else {
                System.out.println("WebSocket handshake failed: No token provided");
                return false;
            }
        }

        System.out.println("WebSocket handshake failed: Invalid request type");
        return false;
    }

    // Hier gebeurt eigenlijk niks
    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {

    }
}
