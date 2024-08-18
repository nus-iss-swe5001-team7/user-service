package com.nus.edu.se.user_service.service;

import com.nus.edu.se.user_service.dao.UserRepository;
import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Users> getUserByName(String userName) {
        try {
            return new ResponseEntity<>(userRepository.findByUserName(userName), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Users(), HttpStatus.BAD_REQUEST);
    }

    public void registerUser(UserRequest userRequest) {
        Users user = Users.builder()
                .userName(userRequest.userName())
                .userRole(userRequest.userRole()).build();
        userRepository.save(user);
    }

}
