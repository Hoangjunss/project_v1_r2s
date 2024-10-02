package com.r2s.project_v1.domain.models;

import jakarta.persistence.Entity;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public enum Role {
    USER,ADMIN
}
