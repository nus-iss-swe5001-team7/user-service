package com.nus.edu.se.user_service.mapper;

import com.nus.edu.se.user_service.dto.UserResponse;
import com.nus.edu.se.user_service.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    public UserResponse formUserResponse(Users usersModel) {
        return new UserResponse(usersModel.getName(), usersModel.getRole());
    }

}
