package com.nus.edu.se.user_service.dto;

public record UserRequest(
        String name,
        String password,
        String email,
        String role) {
}
