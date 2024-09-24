package com.nus.edu.se.user_service.boundary;

import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.model.Users;
import com.nus.edu.se.user_service.service.AuthenticateService;
import com.nus.edu.se.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        userService.validateUserRequest(userRequest);

        log.info("New user registration: {}", userRequest);
        return userService.registerUser(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest) {
        log.info("User login : {}", userRequest);
        return authenticateService.authenticate(userRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String token = userService.resolveToken(request).getBody();
        log.info("User logout : {}", token);
        return userService.logoutUser(token);
    }

    @GetMapping("getUserById/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

}
