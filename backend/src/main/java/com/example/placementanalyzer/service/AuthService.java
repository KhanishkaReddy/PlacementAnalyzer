package com.example.placementanalyzer.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.placementanalyzer.dto.LoginRequest;
import com.example.placementanalyzer.dto.LoginResponse;
import com.example.placementanalyzer.dto.RegisterRequest;
import com.example.placementanalyzer.model.Student;
import com.example.placementanalyzer.model.User;
import com.example.placementanalyzer.repository.StudentRepository;
import com.example.placementanalyzer.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {

        Optional<User> existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Store a password hash instead of the plain-text password.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setCollege(request.getCollege());
        student.setCourse(request.getCourse());
        student.setBranch(request.getBranch());
        student.setYear(request.getYear());
        student.setCgpa(request.getCgpa());

        studentRepository.save(student);

        return savedUser;
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = existingUser.get();

        String storedPassword = user.getPassword();
String enteredPassword = request.getPassword();

if (storedPassword.startsWith("$2a$")
        || storedPassword.startsWith("$2b$")
        || storedPassword.startsWith("$2y$")) {

    if (!passwordEncoder.matches(enteredPassword, storedPassword)) {
        throw new RuntimeException("Invalid email or password");
    }

} else {
    // Support accounts created before BCrypt was introduced.
    if (!storedPassword.equals(enteredPassword)) {
        throw new RuntimeException("Invalid email or password");
    }

    // Upgrade the password after successful login.
    user.setPassword(passwordEncoder.encode(enteredPassword));
    userRepository.save(user);
}

        String token = jwtService.generateToken(user.getEmail());

Student student = studentRepository.findByUserId(user.getId())
        .orElseThrow(() -> new RuntimeException(
                "Student profile not found for this account"));

return new LoginResponse(
        user.getId(),
        student.getId(),
        user.getName(),
        user.getEmail(),
        token
);
    }
}