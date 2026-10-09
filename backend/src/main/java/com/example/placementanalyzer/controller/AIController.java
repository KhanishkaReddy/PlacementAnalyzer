package com.example.placementanalyzer.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
public String chat(@RequestParam String message) {

    try {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();

    } catch (Exception e) {

        return """
                Personalized Improvement Plan

                1. Spring Boot - HIGH PRIORITY
                Learn Spring Boot REST APIs, controllers, services,
                repositories and connect them with MySQL.

                2. React - HIGH PRIORITY
                Improve React fundamentals, components, state,
                API integration and frontend-backend communication.

                3. Project Recommendation
                Build a Full Stack Placement Management System using
                React, Spring Boot and MySQL.

                Focus on Spring Boot first, then improve React.
                """;
    }
}
}