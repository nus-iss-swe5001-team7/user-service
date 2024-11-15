package com.nus.edu.se.user_service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnAuthorizedExceptionTest {

    @Test
    void testUnAuthorizedExceptionWithMessage() {
        // Arrange
        String errorMessage = "Unauthorized access";

        // Act
        UnAuthorizedException exception = new UnAuthorizedException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testUnAuthorizedExceptionWithoutMessage() {
        // Act
        UnAuthorizedException exception = new UnAuthorizedException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
