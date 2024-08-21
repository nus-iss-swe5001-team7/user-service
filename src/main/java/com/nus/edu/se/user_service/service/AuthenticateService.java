package com.nus.edu.se.user_service.service;

import com.nus.edu.se.user_service.dao.UserRepository;
import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.exception.AuthenticationException;
import com.nus.edu.se.user_service.exception.UnAuthorizedException;
import com.nus.edu.se.user_service.mapper.UsersMapper;
import com.nus.edu.se.user_service.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticateService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    public ResponseEntity<UserResponse> authenticate(UserRequest userRequest) {
        try {
            Users user = userRepository.findByEmail(userRequest.email());
            if (user.getPassword().equals(userRequest.password())) {
                return new ResponseEntity<>(usersMapper.formUserResponse(user), HttpStatus.OK);
            } else {
                throw new AuthenticationException("Invalid email or password.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnAuthorizedException("Invalid login credentials");
        }
    }
}
