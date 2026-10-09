package com.example.placementanalyzer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.LoginRequest;
import com.example.placementanalyzer.dto.LoginResponse;
import com.example.placementanalyzer.dto.RegisterRequest;
import com.example.placementanalyzer.model.User;
import com.example.placementanalyzer.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {

        Optional<User> existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = existingUser.get();

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                token
        );
    }
}   