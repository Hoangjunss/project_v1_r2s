package com.r2s.project_v1.domain.service;

import com.r2s.project_v1.application.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.application.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.application.dto.request.user.RefreshToken;
import com.r2s.project_v1.application.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.application.dto.response.user.CreateUserResponse;
import com.r2s.project_v1.domain.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User registration(User user);
    User signIn(User user);
    UserDetails generateRefreshToken(String token);
}
