package com.r2s.project_v1.controller;

import com.r2s.project_v1.dto.userDTO.request.CreateUserRequest;
import com.r2s.project_v1.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("")
    public ResponseEntity<?> registration(@RequestBody CreateUserRequest createUserRequest) {
       return new ResponseEntity<>(userService.registration(createUserRequest), HttpStatus.CREATED);
    }
}
