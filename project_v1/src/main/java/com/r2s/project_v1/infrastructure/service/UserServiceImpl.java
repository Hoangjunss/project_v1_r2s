package com.r2s.project_v1.infrastructure.service;

import com.r2s.project_v1.application.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.application.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.application.dto.request.user.RefreshToken;
import com.r2s.project_v1.application.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.application.dto.response.user.CreateUserResponse;
import com.r2s.project_v1.domain.models.Role;
import com.r2s.project_v1.domain.models.User;
import com.r2s.project_v1.domain.repository.UserRepository;
import com.r2s.project_v1.domain.service.UserService;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.CustomJwtException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public User registration(User user) {
        if(usernameExists(user.getUsername())){
            throw new CustomException(Error.USER_ALREADY_EXISTS);
        }
        user.setId(getGenerationId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        try {
            user= userRepository.save(user);
        }catch (CustomException e){
            throw  new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
        return user;
    }
    @Override
    public User signIn(User user) {
        String name = user.getUsername().trim().toLowerCase();

        // Kiểm tra xem email đã tồn tại chưa
        if (!usernameExists(name)) {
            throw new CustomJwtException(Error.USER_NOT_FOUND);
        }



        User userFind = userRepository.findByUsername(name).orElseThrow();
        if (!passwordEncoder.matches(user.getPassword(), userFind.getPassword())) {
            throw new CustomJwtException(Error.NOT_FOUND);
        }

        return userFind;

    }

    @Override

    public UserDetails generateRefreshToken(String token) {


        // 2. Lấy thông tin từ token cũ
        String username = jwtTokenUtil.extractUsernameToken(token);

        // 3. Tạo refresh token mới
        UserDetails userDetails= ourUserDetailsService.loadUserByUsername(username);

        return  userDetails;
    }





    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        // Use most significant bits and ensure it's within the integer range
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}