package com.r2s.project_v1.domain.service;


import com.r2s.project_v1.domain.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User registration(User user);
    User signIn(User user);
    UserDetails generateRefreshToken(String token);
}
