package com.nus.edu.se.user_service.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-authentication-exception")
    public void triggerAuthenticationException() {
        throw new AuthenticationException("Invalid credentials");
    }

    @GetMapping("/test-conflict-exception")
    public void triggerConflictException() {
        throw new ConflictException("Conflict detected");
    }

    @GetMapping("/test-unauthorized-exception")
    public void triggerUnAuthorizedException() {
        throw new UnAuthorizedException("Unauthorized access");
    }
}
