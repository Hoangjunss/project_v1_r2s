package com.r2s.project_v1.application.service;

import com.r2s.project_v1.application.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.application.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.application.dto.request.user.RefreshToken;
import com.r2s.project_v1.application.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.application.dto.response.user.CreateUserResponse;
import com.r2s.project_v1.domain.service.RoleService;
import com.r2s.project_v1.domain.service.UserService;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.CustomJwtException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.domain.models.Role;
import com.r2s.project_v1.domain.models.User;
import com.r2s.project_v1.domain.repository.UserRepository;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.presentation.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserApplicationService  {


   @Autowired
   private UserService userService;
   @Autowired
   private UserMapper userMapper;
   @Autowired
   private JwtTokenUtil jwtTokenUtil;
   @Autowired
   private RoleService roleService;

    public CreateUserResponse registration(CreateUserRequest createUserRequest) {
        Role role=roleService.findByName(createUserRequest.getRole());
        User user=userMapper.convertCreateUserRequestToUser(createUserRequest,role);

        CreateUserResponse createUserResponse=userMapper.convertUserToCreateUserResponse(userService.registration(user));

        return createUserResponse;
    }

    public AuthenticationResponse signIn(AuthenticationRequest signinRequest) {
        User user=userMapper.convertAuthenticationToUser(signinRequest);
        UserDetails userDetails=userService.signIn(user);
        String jwtToken=jwtTokenUtil.generateToken(userDetails);
        String refreshToken=jwtTokenUtil.generateRefreshToken(userDetails);
      return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken).build();

    }



    public AuthenticationResponse generateRefreshToken(String token) {
           UserDetails user=userService.generateRefreshToken(token);
           String jwtToken=jwtTokenUtil.generateToken(user);
           String refreshToken=jwtTokenUtil.generateRefreshToken(user);

        return  AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken).build();
    }





}
