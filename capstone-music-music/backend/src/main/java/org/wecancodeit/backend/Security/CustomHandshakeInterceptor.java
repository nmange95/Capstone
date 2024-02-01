package org.wecancodeit.backend.Security;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) throws Exception {
        Principal principal = request.getPrincipal();
        if (principal != null) {
            System.out.println("Handshake Principal's name: " + principal.getName());
        } else {
            System.out.println("Handshake Principal is null");
        }
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception) {
        // Post-handshake logic
    }

}
