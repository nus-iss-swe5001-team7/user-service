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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    JwtTokenInterface jwtTokenInterface;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

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
            String encodedPassword = encoder.encode(userRequest.password());
            Users user = Users.builder()
                    .name(userRequest.name())
                    .password(encodedPassword)
                    .email(userRequest.email())
                    .role(userRequest.role()).build();
            userRepository.save(user);
            // Generate JWT token after successful register
            String token = jwtTokenInterface.generateToken(user.getName()).getBody();
            return new ResponseEntity<>(usersMapper.formUserResponse(user, token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConflictException("User or email already exists");
        }
    }

    public ResponseEntity<Users> getUserById(UUID id) {
        try {
            Optional<Users> userOptional = userRepository.findById(id);

            if (userOptional.isPresent()) {
                return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Users(), HttpStatus.BAD_REQUEST);
    }

}
