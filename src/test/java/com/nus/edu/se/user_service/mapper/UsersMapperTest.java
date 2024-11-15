package com.nus.edu.se.user_service.mapper;

import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsersMapperTest {

    private UsersMapper usersMapper;
    private Users mockUser;
    private String token;

    @BeforeEach
    void setUp() {
        usersMapper = new UsersMapper();

        mockUser = Users.builder()
                .userId(UUID.randomUUID())
                .name("testUser")
                .password("securePassword")
                .email("test@example.com")
                .role("customer")
                .build();

        token = "dummyToken";
    }

    @Test
    void testFormUserResponse_Success() {
        // Act
        UserResponse userResponse = usersMapper.formUserResponse(mockUser, token);

        // Assert
        assertNotNull(userResponse);
        assertEquals(mockUser.getUserId().toString(), userResponse.userId());
        assertEquals(mockUser.getName(), userResponse.name());
        assertEquals(mockUser.getRole(), userResponse.role());
        assertEquals(token, userResponse.token());
    }

    @Test
    void testFormUserResponse_NullUser() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                usersMapper.formUserResponse(null, token));

        assertEquals("Cannot invoke \"com.nus.edu.se.user_service.model.Users.getUserId()\" because \"usersModel\" is null", exception.getMessage());
    }

    @Test
    void testFormUserResponse_NullToken() {
        // Act
        UserResponse userResponse = usersMapper.formUserResponse(mockUser, null);

        // Assert
        assertNotNull(userResponse);
        assertEquals(mockUser.getUserId().toString(), userResponse.userId());
        assertEquals(mockUser.getName(), userResponse.name());
        assertEquals(mockUser.getRole(), userResponse.role());
        assertNull(userResponse.token());
    }
}
