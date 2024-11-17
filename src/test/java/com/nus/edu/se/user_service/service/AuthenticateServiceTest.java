package com.nus.edu.se.user_service.service;

import com.nus.edu.se.user_service.dao.UserRepository;
import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.exception.AuthenticationException;
import com.nus.edu.se.user_service.exception.UnAuthorizedException;
import com.nus.edu.se.user_service.mapper.UsersMapper;
import com.nus.edu.se.user_service.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticateServiceTest {

    @InjectMocks
    private AuthenticateService authenticateService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsersMapper usersMapper;

    @Mock
    private JwtTokenInterface jwtTokenInterface;

    private BCryptPasswordEncoder encoder;
    private UserRequest mockUserRequest;
    private Users mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(12);

        mockUserRequest = new UserRequest("testUser", "password123", "test@example.com", "customer");

        mockUser = Users.builder()
                .userId(java.util.UUID.randomUUID())
                .name("testUser")
                .email("test@example.com")
                .password(encoder.encode("password123")) // Encrypt the password for comparison
                .role("customer")
                .build();
    }

    @Test
    void testAuthenticate_Success() {
        // Arrange
        when(userRepository.findByEmail(mockUserRequest.email())).thenReturn(mockUser);
        when(jwtTokenInterface.generateToken(mockUser.getName())).thenReturn(ResponseEntity.ok("dummyToken"));
        when(usersMapper.formUserResponse(mockUser, "dummyToken"))
                .thenReturn(new UserResponse(mockUser.getUserId().toString(), mockUser.getName(), mockUser.getRole(), "dummyToken"));

        // Act
        ResponseEntity<UserResponse> response = authenticateService.authenticate(mockUserRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("dummyToken", response.getBody().token());
        assertEquals(mockUser.getName(), response.getBody().name());
        verify(userRepository, times(1)).findByEmail(mockUserRequest.email());
        verify(jwtTokenInterface, times(1)).generateToken(mockUser.getName());
        verify(usersMapper, times(1)).formUserResponse(mockUser, "dummyToken");
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        // Arrange
        when(userRepository.findByEmail(mockUserRequest.email())).thenReturn(mockUser);

        // Modify password to simulate invalid credentials
        mockUserRequest = new UserRequest("testUser", "wrongPassword", "test@example.com", "customer");

        // Act & Assert
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () ->
                authenticateService.authenticate(mockUserRequest));

        assertEquals("Invalid login credentials", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(mockUserRequest.email());
        verify(jwtTokenInterface, never()).generateToken(anyString());
    }


    @Test
    void testAuthenticate_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(mockUserRequest.email())).thenReturn(null);

        // Act & Assert
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () ->
                authenticateService.authenticate(mockUserRequest));

        assertEquals("Invalid login credentials", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(mockUserRequest.email());
        verify(jwtTokenInterface, never()).generateToken(anyString());
    }
}
