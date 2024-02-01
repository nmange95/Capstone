package org.wecancodeit.backend.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey jwtSecretKey;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    }

    public String authenticateUser(User user) {
        Optional<User> foundUserOpt = userRepository.findByUsername(user.getUsername());
        if (foundUserOpt.isPresent()) {
            User foundUser = foundUserOpt.get();
            if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {

                return Jwts.builder()
                        .setSubject(foundUser.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                        .signWith(jwtSecretKey)
                        .compact();
            }
        }
        throw new RuntimeException("Authentication failed");
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        // Create an Authentication object
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }
}
