package com.example.placementanalyzer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.UserDTO;
import com.example.placementanalyzer.model.User;
import com.example.placementanalyzer.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(User user) {

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    public Optional<UserDTO> getUserById(Long id) {

        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<User> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    private UserDTO convertToDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }
}