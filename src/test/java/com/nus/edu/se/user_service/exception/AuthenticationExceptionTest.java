package com.nus.edu.se.user_service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationExceptionTest {

    @Test
    void testAuthenticationExceptionMessage() {
        // Arrange
        String errorMessage = "Invalid credentials";

        // Act
        AuthenticationException exception = new AuthenticationException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testAuthenticationExceptionWithoutMessage() {
        // Act
        AuthenticationException exception = new AuthenticationException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
