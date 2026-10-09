package com.example.placementanalyzer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.placementanalyzer.dto.LoginRequest;
import com.example.placementanalyzer.dto.LoginResponse;
import com.example.placementanalyzer.dto.RegisterRequest;
import com.example.placementanalyzer.dto.UserDTO;
import com.example.placementanalyzer.model.User;
import com.example.placementanalyzer.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
public ResponseEntity<UserDTO> register(
        @RequestBody RegisterRequest request) {

    User registeredUser = authService.register(request);

    UserDTO response = new UserDTO();

    response.setId(registeredUser.getId());
    response.setName(registeredUser.getName());
    response.setEmail(registeredUser.getEmail());

    return ResponseEntity.ok(response);
}

   @PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody LoginRequest request) {

    try {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
}
}