package com.nus.edu.se.user_service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {

    @Test
    void testConflictExceptionWithMessage() {
        // Arrange
        String errorMessage = "Conflict occurred";

        // Act
        ConflictException exception = new ConflictException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConflictExceptionWithoutMessage() {
        // Act
        ConflictException exception = new ConflictException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
