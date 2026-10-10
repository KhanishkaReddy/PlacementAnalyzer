package com.example.placementanalyzer.dto;

public class LoginResponse {

    private Long id;
    private Long studentId;
    private String name;
    private String email;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            Long id,
            Long studentId,
            String name,
            String email,
            String token) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}