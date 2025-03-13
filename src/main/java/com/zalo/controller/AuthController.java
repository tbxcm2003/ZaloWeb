package com.zalo.controller;

import com.zalo.dto.LoginRequest;
import com.zalo.dto.LoginResponse;
import com.zalo.dto.RegisterRequest;
import com.zalo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginWithPassword(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qr/generate")
    public ResponseEntity<String> generateQRCode() {

        String userId = "temp-user-id"; // Placeholder
        String qrToken = authService.generateQRLoginCode(userId);
        return ResponseEntity.ok(qrToken);
    }

    @PostMapping("/qr/verify")
    public ResponseEntity<LoginResponse> verifyQRLogin(@RequestParam String qrToken) {
        LoginResponse response = authService.verifyQRLogin(qrToken);
        return ResponseEntity.ok(response);
    }

    // Add to AuthController.java
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        LoginResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}