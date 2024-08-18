package com.nus.edu.se.user_service.boundary;

import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.model.Users;
import com.nus.edu.se.user_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("get/{userName}")
    public ResponseEntity<Users> getCustomer(@PathVariable String userName) {
        return userService.getUserByName(userName);
    }

    @PostMapping
    public void registerUser(@RequestBody UserRequest userRequest) {
        log.info("new user registration {}", userRequest);
        userService.registerUser(userRequest);
    }
}
