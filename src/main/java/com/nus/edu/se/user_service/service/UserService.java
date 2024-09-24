package com.nus.edu.se.user_service.service;

import com.nus.edu.se.user_service.dao.UserRepository;
import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.exception.AuthenticationException;
import com.nus.edu.se.user_service.exception.ConflictException;
import com.nus.edu.se.user_service.mapper.UsersMapper;
import com.nus.edu.se.user_service.model.Users;
import jakarta.servlet.http.HttpServletRequest;
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
            throw new ConflictException("Something went wrong");
        }
    }

    public ResponseEntity<String> logoutUser(String token) {
        try {
            jwtTokenInterface.blacklistToken(token);
            return new ResponseEntity<>("User has logged out.", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConflictException("Something went wrong");
        }
    }

    public ResponseEntity<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return new ResponseEntity<>(bearerToken.substring(7), HttpStatus.OK);
        }
        return new ResponseEntity<>("No Token existed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Users> getUserById(UUID id) {
        try {
            Optional<Users> userOptional = Optional.ofNullable(userRepository.findByUserId(id));

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

    public void validateUserRequest(UserRequest userRequest) {
        if (userRequest == null) {
            log.error("UserRequest is null");
            throw new AuthenticationException("UserRequest cannot be null");
        }

        if (isNullOrEmpty(userRequest.name()) || isNullOrEmpty(userRequest.password()) ||
                isNullOrEmpty(userRequest.email()) || isNullOrEmpty(userRequest.role())) {

            log.error("Invalid user registration data: {}", userRequest);
            throw new AuthenticationException("Name, password, role, and email must be defined and not empty");
        }

        if (userNameOrEmailExist(userRequest)) {
            log.error("Name or Email exists: {}", userRequest.email());
            throw new ConflictException("UserName or email already exists");
        }
    }


    private boolean userNameOrEmailExist(UserRequest userRequest) {
        Users userWithSameName = userRepository.findByName(userRequest.name());
        Users userWithSameEmail = userRepository.findByEmail(userRequest.email());
        return userWithSameName != null || userWithSameEmail != null;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
