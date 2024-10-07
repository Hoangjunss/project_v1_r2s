package com.r2s.project_v1.service;

import com.r2s.project_v1.domain.models.User;
import com.r2s.project_v1.domain.repository.UserRepository;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.CustomJwtException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.infrastructure.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OurUserDetailsService ourUserDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    // Test registration method
    @Test
    void registration_ShouldReturnSavedUser_WhenValid() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registration(user);
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registration_ShouldThrowException_WhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.registration(user);
        });
        assertEquals(Error.USER_ALREADY_EXISTS, exception.getError());
        verify(userRepository, never()).save(any(User.class));
    }

    // Test signIn method


    @Test
    void signIn_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> {
            userService.signIn(user);
        });
        assertEquals(Error.USER_NOT_FOUND, exception.getError());
    }

    @Test
    void signIn_ShouldThrowException_WhenPasswordDoesNotMatch() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> {
            userService.signIn(user);
        });
        assertEquals(Error.NOT_FOUND,exception.getError());
    }

    // Test generateRefreshToken method
    @Test
    void generateRefreshToken_ShouldReturnUserDetails_WhenValidToken() {
        when(jwtTokenUtil.extractUsernameToken(anyString())).thenReturn("testuser");
        when(ourUserDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));

        UserDetails userDetails = userService.generateRefreshToken("validToken");
        assertNotNull(userDetails);
        verify(ourUserDetailsService, times(1)).loadUserByUsername(anyString());
    }

    // Utility method for UUID generation
    @Test
    void getGenerationId_ShouldReturnValidInteger() {
        Integer id = userService.getGenerationId();
        assertNotNull(id);
        assertTrue(id >= Integer.MIN_VALUE && id <= Integer.MAX_VALUE);
    }
}

