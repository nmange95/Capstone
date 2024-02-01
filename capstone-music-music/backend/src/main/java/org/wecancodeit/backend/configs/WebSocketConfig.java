package org.wecancodeit.backend.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.wecancodeit.backend.Security.CustomHandshakeHandler;
import org.wecancodeit.backend.Security.CustomHandshakeInterceptor;
import org.wecancodeit.backend.services.AuthService;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private AuthService authService;

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setHandshakeHandler(new CustomHandshakeHandler(authService))
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setInterceptors(new CustomHandshakeInterceptor());

    }

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
        registry.setUserDestinationPrefix("/user");
    }
}
