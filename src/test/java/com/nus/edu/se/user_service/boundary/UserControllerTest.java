package com.nus.edu.se.user_service.boundary;

import com.nus.edu.se.user_service.dto.UserRequest;
import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.model.Users;
import com.nus.edu.se.user_service.service.AuthenticateService;
import com.nus.edu.se.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticateService authenticateService;

    private MockMvc mockMvc;

    private UserRequest mockUserRequest;
    private UserResponse mockUserResponse;
    private Users mockUser;
    private UUID mockUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockUserId = UUID.randomUUID();
        mockUserRequest = new UserRequest("username", "password", "email@example.com", "customer");
        mockUserResponse = new UserResponse(mockUserId.toString(), "username", "customer", "token");
        mockUser = Users.builder()
                .userId(mockUserId)
                .name("username")
                .password("password")
                .email("email@example.com")
                .role("customer")
                .build();
    }

    @Test
    void testGetUserByName_Success() throws Exception {
        when(userService.getUserByName("username")).thenReturn(ResponseEntity.ok(mockUser));

        mockMvc.perform(get("/user/get/username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(mockUserId.toString()))
                .andExpect(jsonPath("$.name").value("username"))
                .andExpect(jsonPath("$.email").value("email@example.com"))
                .andExpect(jsonPath("$.role").value("customer"));

        verify(userService, times(1)).getUserByName("username");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(userService.registerUser(any(UserRequest.class))).thenReturn(ResponseEntity.ok(mockUserResponse));

        mockMvc.perform(post("/user/register")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "username",
                                  "password": "password",
                                  "email": "email@example.com",
                                  "role": "customer"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(mockUserId.toString()))
                .andExpect(jsonPath("$.name").value("username"))
                .andExpect(jsonPath("$.role").value("customer"))
                .andExpect(jsonPath("$.token").value("token"));

        verify(userService, times(1)).validateUserRequest(any(UserRequest.class));
        verify(userService, times(1)).registerUser(any(UserRequest.class));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        when(authenticateService.authenticate(any(UserRequest.class))).thenReturn(ResponseEntity.ok(mockUserResponse));

        mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "username",
                                  "password": "password",
                                  "email": "email@example.com",
                                  "role": "customer"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(mockUserId.toString()))
                .andExpect(jsonPath("$.name").value("username"))
                .andExpect(jsonPath("$.role").value("customer"))
                .andExpect(jsonPath("$.token").value("token"));

        verify(authenticateService, times(1)).authenticate(any(UserRequest.class));
    }

    @Test
    void testLogoutUser_Success() throws Exception {
        when(userService.resolveToken(any(HttpServletRequest.class))).thenReturn(ResponseEntity.ok("token"));
        when(userService.logoutUser("token")).thenReturn(ResponseEntity.ok("Logout successful"));

        mockMvc.perform(post("/user/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));

        verify(userService, times(1)).resolveToken(any(HttpServletRequest.class));
        verify(userService, times(1)).logoutUser("token");
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.resolveToken(any(HttpServletRequest.class))).thenReturn(ResponseEntity.ok("token"));
        when(userService.getUserById(eq(mockUserId), eq("token"))).thenReturn(ResponseEntity.ok(mockUser));

        mockMvc.perform(get("/user/getUserById/" + mockUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(mockUserId.toString()))
                .andExpect(jsonPath("$.name").value("username"))
                .andExpect(jsonPath("$.email").value("email@example.com"))
                .andExpect(jsonPath("$.role").value("customer"));

        verify(userService, times(1)).resolveToken(any(HttpServletRequest.class));
        verify(userService, times(1)).getUserById(eq(mockUserId), eq("token"));
    }
}
