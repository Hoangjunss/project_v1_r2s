package com.r2s.project_v1.presentation.mapper;

import com.r2s.project_v1.application.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.application.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.application.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.application.dto.response.user.CreateUserResponse;
import com.r2s.project_v1.domain.models.Role;
import com.r2s.project_v1.domain.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;
    public User convertCreateUserRequestToUser(CreateUserRequest createUserRequest,Role role){
        User user= User.builder()
                .email(createUserRequest.getEmail())
                .fullname(createUserRequest.getFullname())
                .password(createUserRequest.getPassword())
                .username(createUserRequest.getUsername())
                .role(role).build();
        return user;
    }
    public CreateUserResponse convertUserToCreateUserResponse(User user){
       CreateUserResponse createUserResponse= CreateUserResponse.builder()
               .id(user.getId())
               .email(user.getEmail())
               .fullname(user.getFullname())
               .username(user.getUsername())
               .role(user.getRole().getName())
               .build();
        return createUserResponse;
    }
    public User convertAuthenticationToUser(AuthenticationRequest authenticationRequest){
       User user= User.builder()
               .username(authenticationRequest.getName())
               .password(authenticationRequest.getPassword())
               .build();
        return user;
    }
}
