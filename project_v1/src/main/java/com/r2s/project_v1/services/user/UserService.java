package com.r2s.project_v1.services.user;

import com.r2s.project_v1.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.dto.request.user.RefreshToken;
import com.r2s.project_v1.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.dto.response.user.CreateUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    CreateUserResponse registration(CreateUserRequest createUserRequest);
    AuthenticationResponse signIn(AuthenticationRequest signinRequest);
    AuthenticationResponse generateRefreshToken(RefreshToken token);
}
