package com.nus.edu.se.user_service.boundary;

import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.exception.AuthenticationException;
import com.nus.edu.se.user_service.model.Users;
import com.nus.edu.se.user_service.service.AuthenticateService;
import com.nus.edu.se.user_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticateService authenticateService;

    @GetMapping("get/{name}")
    public ResponseEntity<Users> getUser(@PathVariable String name) {
        return userService.getUserByName(name);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        validateUserRequest(userRequest);

        log.info("New user registration: {}", userRequest);
        return userService.registerUser(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest) {
        log.info("User login : {}", userRequest);
        return authenticateService.authenticate(userRequest);
    }

    private void validateUserRequest(UserRequest userRequest) {
        if (userRequest == null) {
            log.error("UserRequest is null");
            throw new AuthenticationException("UserRequest cannot be null");
        }

        if (isNullOrEmpty(userRequest.name()) || isNullOrEmpty(userRequest.password()) ||
                isNullOrEmpty(userRequest.email()) || isNullOrEmpty(userRequest.role())) {

            log.error("Invalid user registration data: {}", userRequest);
            throw new AuthenticationException("Name, password, role, and email must be defined and not empty");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

}
