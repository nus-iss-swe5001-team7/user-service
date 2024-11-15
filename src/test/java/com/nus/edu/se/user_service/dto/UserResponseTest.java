package com.nus.edu.se.user_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void testUserResponseCreation() {
        // Arrange
        String userId = "12345";
        String name = "testUser";
        String role = "customer";
        String token = "dummyToken";

        // Act
        UserResponse userResponse = new UserResponse(userId, name, role, token);

        // Assert
        assertEquals(userId, userResponse.userId());
        assertEquals(name, userResponse.name());
        assertEquals(role, userResponse.role());
        assertEquals(token, userResponse.token());
    }

    @Test
    void testUserResponseEquality() {
        // Arrange
        UserResponse userResponse1 = new UserResponse("12345", "testUser", "customer", "dummyToken");
        UserResponse userResponse2 = new UserResponse("12345", "testUser", "customer", "dummyToken");

        // Act & Assert
        assertEquals(userResponse1, userResponse2);
        assertEquals(userResponse1.hashCode(), userResponse2.hashCode());
    }

    @Test
    void testUserResponseToString() {
        // Arrange
        UserResponse userResponse = new UserResponse("12345", "testUser", "customer", "dummyToken");

        // Act
        String userResponseString = userResponse.toString();

        // Assert
        assertTrue(userResponseString.contains("12345"));
        assertTrue(userResponseString.contains("testUser"));
        assertTrue(userResponseString.contains("customer"));
        assertTrue(userResponseString.contains("dummyToken"));
    }
}
