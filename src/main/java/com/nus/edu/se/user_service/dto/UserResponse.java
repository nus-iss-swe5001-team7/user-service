package com.nus.edu.se.user_service.dto;

public record UserResponse(
        String userId,
        String name,
        String role) {
}
