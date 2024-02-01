package org.wecancodeit.backend.controllers;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.services.AuthService;
import org.wecancodeit.backend.models.AuthResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginUser) {
        try {
            String token = authService.authenticateUser(loginUser);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/checkStatus")
    public ResponseEntity<String> checkAuthStatus(Principal principal) {
        if (principal != null) {
            // User is logged in
            return ResponseEntity.ok(principal.getName());
        } else {
            // User is not logged in
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}