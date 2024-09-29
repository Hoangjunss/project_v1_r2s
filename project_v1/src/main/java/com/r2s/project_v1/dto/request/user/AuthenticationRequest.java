package com.r2s.project_v1.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthenticationRequest {
    private String name;
    private String password;

}