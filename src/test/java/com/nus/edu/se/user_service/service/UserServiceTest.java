package com.nus.edu.se.user_service.service;

import com.nus.edu.se.user_service.dao.UserRepository;
import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.exception.AuthenticationException;
import com.nus.edu.se.user_service.exception.ConflictException;
import com.nus.edu.se.user_service.mapper.UsersMapper;
import com.nus.edu.se.user_service.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

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
                .userId(UUID.randomUUID())
                .name("testUser")
                .password(encoder.encode("password123"))
                .email("test@example.com")
                .role("customer")
                .build();
    }

    @Test
    void testGetUserByName_Success() {
        when(userRepository.findByName("testUser")).thenReturn(mockUser);

        ResponseEntity<Users> response = userService.getUserByName("testUser");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody().getName());
        verify(userRepository, times(1)).findByName("testUser");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(mockUserRequest.email())).thenReturn(null);
        when(userRepository.findByName(mockUserRequest.name())).thenReturn(null);
        when(jwtTokenInterface.generateToken("testUser")).thenReturn(ResponseEntity.ok("dummyToken"));
        when(usersMapper.formUserResponse(any(Users.class), eq("dummyToken")))
                .thenReturn(new UserResponse(mockUser.getUserId().toString(), "testUser", "customer", "dummyToken"));

        ResponseEntity<UserResponse> response = userService.registerUser(mockUserRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("testUser", response.getBody().name());
        assertEquals("dummyToken", response.getBody().token());
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testRegisterUser_ConflictException() {
        when(userRepository.findByEmail(mockUserRequest.email())).thenReturn(mockUser);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                userService.registerUser(mockUserRequest));

        assertEquals("Something went wrong", exception.getMessage());
    }

    @Test
    void testLogoutUser_Success() {
        // Arrange
        when(jwtTokenInterface.blacklistToken("dummyToken")).thenReturn(ResponseEntity.ok("Token blacklisted"));

        // Act
        ResponseEntity<String> response = userService.logoutUser("dummyToken");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User has logged out.", response.getBody());

        verify(jwtTokenInterface, times(1)).blacklistToken("dummyToken");
    }

    @Test
    void testResolveToken_Success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer dummyToken");

        ResponseEntity<String> response = userService.resolveToken(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("dummyToken", response.getBody());
    }

    @Test
    void testResolveToken_NoToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<String> response = userService.resolveToken(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("No Token existed", response.getBody());
    }

    @Test
    void testGetUserById_Authenticated() throws Exception {
        when(jwtTokenInterface.validateToken("dummyToken")).thenReturn(ResponseEntity.ok(true));
        when(userRepository.findByUserId(mockUser.getUserId())).thenReturn(mockUser);

        ResponseEntity<Users> response = userService.getUserById(mockUser.getUserId(), "dummyToken");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody().getName());
        verify(jwtTokenInterface, times(1)).validateToken("dummyToken");
    }

    @Test
    void testGetUserById_UnAuthenticated() {
        when(jwtTokenInterface.validateToken("dummyToken")).thenReturn(ResponseEntity.ok(false));

        assertThrows(org.apache.tomcat.websocket.AuthenticationException.class, () ->
                userService.getUserById(mockUser.getUserId(), "dummyToken"));
    }

    @Test
    void testValidateUserRequest_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.findByName("testUser")).thenReturn(null);

        assertDoesNotThrow(() -> userService.validateUserRequest(mockUserRequest));
    }

    @Test
    void testValidateUserRequest_NullRequest() {
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                userService.validateUserRequest(null));

        assertEquals("UserRequest cannot be null", exception.getMessage());
    }

    @Test
    void testValidateUserRequest_ExistingUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                userService.validateUserRequest(mockUserRequest));

        assertEquals("UserName or email already exists", exception.getMessage());
    }
}
