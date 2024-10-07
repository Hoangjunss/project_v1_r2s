package com.r2s.project_v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.r2s.project_v1.application.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.application.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.application.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.application.dto.response.user.CreateUserResponse;
import com.r2s.project_v1.application.service.UserApplicationService;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.presentation.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApplicationService userService;
    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private CreateUserRequest createUserRequest;
    private AuthenticationRequest signInRequest;

    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("user123");
        createUserRequest.setEmail("user123@gmail.com");
        createUserRequest.setPassword("password");
        createUserRequest.setFullname("User Test");
        createUserRequest.setRole("USER");

        signInRequest = new AuthenticationRequest();
        signInRequest.setName("user123");
        signInRequest.setPassword("password");
    }

    @Test
    void registration_shouldReturn201() throws Exception {
        // Giả lập phản hồi khi gọi userService.registration
        CreateUserResponse response = new CreateUserResponse();
        response.setId(1); // Giả lập id người dùng
        response.setUsername("user123");
        response.setEmail("user123@gmail.com");
        response.setFullname("User Test");
        response.setRole("USER");

        when(userService.registration(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user123\",\"email\":\"user123@gmail.com\",\"password\":\"password\",\"fullname\":\"User Test\",\"role\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1)) // Kiểm tra id
                .andExpect(jsonPath("$.username").value("user123")) // Kiểm tra username
                .andExpect(jsonPath("$.email").value("user123@gmail.com")) // Kiểm tra email
                .andExpect(jsonPath("$.fullname").value("User Test")) // Kiểm tra fullname
                .andExpect(jsonPath("$.role").value("USER")); // Kiểm tra role

        verify(userService, times(1)).registration(any(CreateUserRequest.class));
    }


    @Test
    void signIn_shouldReturn200() throws Exception {
        // Giả lập phản hồi khi gọi userService.signIn
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken("jwt-token");
        authResponse.setRefreshToken("refresh-token");

        when(userService.signIn(any(AuthenticationRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"user123\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

        verify(userService, times(1)).signIn(any(AuthenticationRequest.class));
    }

    @Test
    void refreshtoken_shouldReturn200() throws Exception {
        // Giả lập phản hồi khi gọi userService.generateRefreshToken
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken("new-jwt-token");
        authResponse.setRefreshToken("new-refresh-token");

        when(userService.generateRefreshToken(anyString()))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/user/refreshtoken")
                        .param("token", "refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new-jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

        verify(userService, times(1)).generateRefreshToken(anyString());
    }

    @Test
    void testH_shouldReturn200() throws Exception {
        mockMvc.perform(post("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("oki"));
    }
}
