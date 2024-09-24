package com.r2s.project_v1.dto.userDTO.response;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateUserResponse {
    private Integer id;
    private String username;
    private String email;
    private String fullname;
    private String role;
}
