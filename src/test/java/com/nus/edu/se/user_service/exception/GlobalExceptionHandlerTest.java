package com.nus.edu.se.user_service.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({GlobalExceptionHandler.class, TestController.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleAuthenticationException() throws Exception {
        mockMvc.perform(get("/test-authentication-exception")
                        .requestAttr("exception", new AuthenticationException("Invalid credentials")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testHandleConflictException() throws Exception {
        mockMvc.perform(get("/test-conflict-exception")
                        .requestAttr("exception", new ConflictException("Conflict detected")))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conflict detected"));
    }

    @Test
    void testHandleUnAuthorizedException() throws Exception {
        mockMvc.perform(get("/test-unauthorized-exception")
                        .requestAttr("exception", new UnAuthorizedException("Unauthorized access")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized access"));
    }

}

