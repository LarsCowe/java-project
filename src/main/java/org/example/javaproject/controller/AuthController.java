package org.example.javaproject.controller;

import org.example.javaproject.dto.AuthRequest;
import org.example.javaproject.dto.AuthResponse;
import org.example.javaproject.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String username = request.getUsername();

        // Simpele validatie -> username mag niet leeg zijn
        if (username != null || username.trim().isEmpty()) {
            System.out.println("Login poging met lege username");
            return ResponseEntity.badRequest().build();
        }

        String token = jwtUtil.generateToken(username);

        System.out.println("Token gegenereerd voor user: " + username);

        return ResponseEntity.ok(new AuthResponse(token, username));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(isValid);
    }






}
