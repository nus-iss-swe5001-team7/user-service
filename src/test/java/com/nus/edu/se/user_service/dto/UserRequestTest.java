package com.nus.edu.se.user_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {

    @Test
    void testUserRequestCreation() {
        // Arrange
        String name = "testUser";
        String password = "securePassword";
        String email = "testUser@example.com";
        String role = "customer";

        // Act
        UserRequest userRequest = new UserRequest(name, password, email, role);

        // Assert
        assertEquals(name, userRequest.name());
        assertEquals(password, userRequest.password());
        assertEquals(email, userRequest.email());
        assertEquals(role, userRequest.role());
    }

    @Test
    void testUserRequestEquality() {
        // Arrange
        UserRequest userRequest1 = new UserRequest("test", "password", "test@example.com", "user");
        UserRequest userRequest2 = new UserRequest("test", "password", "test@example.com", "user");

        // Act & Assert
        assertEquals(userRequest1, userRequest2);
        assertEquals(userRequest1.hashCode(), userRequest2.hashCode());
    }

    @Test
    void testUserRequestToString() {
        // Arrange
        UserRequest userRequest = new UserRequest("testUser", "securePassword", "testUser@example.com", "customer");

        // Act
        String userRequestString = userRequest.toString();

        // Assert
        assertTrue(userRequestString.contains("testUser"));
        assertTrue(userRequestString.contains("securePassword"));
        assertTrue(userRequestString.contains("testUser@example.com"));
        assertTrue(userRequestString.contains("customer"));
    }
}
