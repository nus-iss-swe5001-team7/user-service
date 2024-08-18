package com.nus.edu.se.user_service.dao;

import com.nus.edu.se.user_service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUserName(String userName);
}
