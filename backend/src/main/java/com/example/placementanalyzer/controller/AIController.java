package com.example.placementanalyzer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AIController {

    private static final Logger logger =
            LoggerFactory.getLogger(AIController.class);

    private final ChatClient chatClient;

    public AIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam String message) {

        try {
            String response = chatClient
                    .prompt()
                    .user(message)
                    .call()
                    .content();

            if (response == null || response.isBlank()) {
                logger.error("Gemini returned an empty response.");

                return ResponseEntity
                        .status(HttpStatus.BAD_GATEWAY)
                        .body("Gemini returned an empty response.");
            }

            logger.info("Gemini generated a recommendation successfully.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Gemini API request failed.", e);

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("Gemini request failed: "
                            + e.getClass().getSimpleName()
                            + ". Check the backend terminal for details.");
        }
    }
}