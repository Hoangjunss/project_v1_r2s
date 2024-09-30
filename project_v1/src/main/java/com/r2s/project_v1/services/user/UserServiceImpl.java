package com.r2s.project_v1.services.user;

import com.r2s.project_v1.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.dto.request.user.RefreshToken;
import com.r2s.project_v1.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.dto.response.user.CreateUserResponse;
import com.r2s.project_v1.exception.CustomException;
import com.r2s.project_v1.exception.CustomJwtException;
import com.r2s.project_v1.exception.Error;
import com.r2s.project_v1.models.Role;
import com.r2s.project_v1.models.User;
import com.r2s.project_v1.repository.UserRepository;
import com.r2s.project_v1.security.JwtTokenUtil;
import com.r2s.project_v1.security.OurUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .role(Role.valueOf(createUserRequest.getRole()))
                .build();
        User userSave=new User();
        try {
            userSave= userRepository.save(user);
        }catch (CustomException e){
        throw  new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
        CreateUserResponse createUserResponse= modelMapper.map(userSave, CreateUserResponse.class) ;
        createUserResponse.setRole(userSave.getRole().toString());
        return createUserResponse;
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

    public AuthenticationResponse generateRefreshToken(RefreshToken token) {


        // 2. Lấy thông tin từ token cũ
        String username = jwtTokenUtil.extractUsernameToken(token.getToken());

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
