package com.r2s.project_v1.dto.response.user;


import com.r2s.project_v1.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthenticationResponse {

    private String token;
    private String refreshToken;

}