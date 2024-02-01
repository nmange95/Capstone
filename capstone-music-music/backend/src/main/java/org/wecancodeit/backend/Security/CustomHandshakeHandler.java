package org.wecancodeit.backend.Security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.wecancodeit.backend.services.AuthService;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final AuthService authService;

    public CustomHandshakeHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) {
        String token = extractToken(request);
        if (token != null && authService.validateToken(token)) {
            Authentication authentication = authService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication::getName;
        }
        return null; // Or handle invalid token scenario
    }

    private String extractToken(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        System.out.println(headers);
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
