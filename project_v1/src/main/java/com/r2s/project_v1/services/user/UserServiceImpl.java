package com.r2s.project_v1.services.user;

import com.r2s.project_v1.dto.userDTO.request.AuthenticationRequest;
import com.r2s.project_v1.dto.userDTO.request.CreateUserRequest;
import com.r2s.project_v1.dto.userDTO.response.AuthenticationResponse;
import com.r2s.project_v1.dto.userDTO.response.CreateUserResponse;
import com.r2s.project_v1.exception.CustomException;
import com.r2s.project_v1.exception.CustomJwtException;
import com.r2s.project_v1.exception.Error;
import com.r2s.project_v1.models.User;
import com.r2s.project_v1.repository.UserRepository;
import com.r2s.project_v1.security.JwtTokenUtil;
import com.r2s.project_v1.security.OurUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@Service
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
    public CreateUserResponse registration(CreateUserRequest createUserRequest) {
        if(usernameExists(createUserRequest.getUsername())){
            throw new CustomException(Error.USER_ALREADY_EXISTS);
        }
        User user=User.builder()
                .id(getGenerationId())
                .email(createUserRequest.getEmail())
                .username(createUserRequest.getUsername())
                .fullname(createUserRequest.getFullname())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();
        User userSave=new User();
        try {
            userSave= userRepository.save(user);
        }catch (CustomException e){

        }
        return modelMapper.map(userSave, CreateUserResponse.class) ;
    }
    @Override
    public AuthenticationResponse signIn(AuthenticationRequest signinRequest) {
        String name = signinRequest.getName().trim().toLowerCase();

        // Kiểm tra xem email đã tồn tại chưa
        if (!usernameExists(name)) {
           throw new CustomJwtException(Error.USER_NOT_FOUND);
        }



         User user = userRepository.findByUsername(name).orElseThrow();
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new CustomJwtException(Error.NOT_FOUND);
        }
        var jwt = jwtTokenUtil.generateToken(user);
        var refreshToken = jwtTokenUtil.generateRefreshToken( user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public AuthenticationResponse generateRefreshToken(String token) {
        Claims claims = JwtTokenUtil.extractClaims(token, Function.identity());
        if (claims == null) {
            throw new RuntimeException("Token không hợp lệ");
        }

        // 2. Lấy thông tin từ token cũ
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();

        // 3. Tạo refresh token mới
        UserDetails userDetails= ourUserDetailsService.loadUserByUsername(username);
        String jwttoken= jwtTokenUtil.generateToken(userDetails);
        String refreshToken=jwtTokenUtil.generateRefreshToken(userDetails);
        // 4. Trả về đối tượng phản hồi chứa refresh token
        return  AuthenticationResponse.builder().token(jwttoken).refreshToken(jwttoken).build();
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
