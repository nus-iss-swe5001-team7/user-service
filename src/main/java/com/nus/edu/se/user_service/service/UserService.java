package com.nus.edu.se.user_service.service;

import com.nus.edu.se.user_service.dao.UserRepository;
import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.exception.ConflictException;
import com.nus.edu.se.user_service.mapper.UsersMapper;
import com.nus.edu.se.user_service.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    public ResponseEntity<Users> getUserByName(String userName) {
        try {
            return new ResponseEntity<>(userRepository.findByName(userName), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Users(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<UserResponse> registerUser(UserRequest userRequest) {
        try {
            Users user = Users.builder()
                    .name(userRequest.name())
                    .password(userRequest.password())
                    .email(userRequest.email())
                    .role(userRequest.role()).build();
            userRepository.save(user);
            return new ResponseEntity<>(usersMapper.formUserResponse(user), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConflictException("User or email already exists");
        }
    }

}
