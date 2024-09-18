package com.r2s.project_v1.services.user;

import com.r2s.project_v1.dto.userDTO.request.AuthenticationRequest;
import com.r2s.project_v1.dto.userDTO.request.CreateUserRequest;
import com.r2s.project_v1.dto.userDTO.response.AuthenticationResponse;
import com.r2s.project_v1.dto.userDTO.response.CreateUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    CreateUserResponse registration(CreateUserRequest createUserRequest);
    AuthenticationResponse signIn(AuthenticationRequest signinRequest);
    AuthenticationResponse generateRefreshToken(String token);
}
