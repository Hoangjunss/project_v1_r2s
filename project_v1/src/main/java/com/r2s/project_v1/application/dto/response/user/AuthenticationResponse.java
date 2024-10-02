package com.r2s.project_v1.application.dto.response.user;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthenticationResponse {

    private String token;
    private String refreshToken;

}