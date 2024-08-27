package com.nus.edu.se.user_service.dao;

import com.nus.edu.se.user_service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByName(String name);

    Users findByEmail(String email);

//    Optional<Users> findById(UUID user_id);
}
