package com.nus.edu.se.user_service.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.persistence.EntityManager;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UsersTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testUsersEntityCreation() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String name = "testUser";
        String password = "securePassword";
        String email = "testUser@example.com";
        String role = "customer";

        // Act
        Users user = Users.builder()
                .userId(userId)
                .name(name)
                .password(password)
                .email(email)
                .role(role)
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(name, user.getName());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());
    }

    @Test
    void testUsersEntityPersistence() {
        // Arrange
        Users user = Users.builder()
                .name("testUser")
                .password("securePassword")
                .email("testUser@example.com")
                .role("customer")
                .build();

        // Act
        entityManager.persist(user);
        entityManager.flush();

        // Retrieve the user from the database
        Users retrievedUser = entityManager.find(Users.class, user.getUserId());

        // Assert
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getName());
        assertEquals("securePassword", retrievedUser.getPassword());
        assertEquals("testUser@example.com", retrievedUser.getEmail());
        assertEquals("customer", retrievedUser.getRole());
    }

}
