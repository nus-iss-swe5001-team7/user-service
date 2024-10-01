package com.nus.edu.se.user_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("API-GATEWAY")
public interface JwtTokenInterface {
    @PostMapping("jwt/generateToken")
    ResponseEntity<String> generateToken(@RequestBody String userName);

    @PostMapping("jwt/blacklistToken")
    ResponseEntity<String> blacklistToken(@RequestBody String token);

    @PostMapping("jwt/validateToken")
    ResponseEntity<Boolean> validateToken(@RequestBody String token);
}
