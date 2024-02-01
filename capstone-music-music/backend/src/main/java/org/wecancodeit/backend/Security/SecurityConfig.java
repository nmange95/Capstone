package org.wecancodeit.backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wecancodeit.backend.services.AuthService;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthService authService) throws Exception {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(authService);

        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll() // Permit H2 console access
                        .requestMatchers("/api/auth/**").permitAll() // Permit all requests to auth endpoints
                        .requestMatchers("/chat/**").permitAll() //
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/audio/**").permitAll()
                        .anyRequest().permitAll() // Require auth for all other requests
                )
                .headers(headers -> headers // Set headers to allow frame options for H2 console
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()));

        return http.build();
    }
}
