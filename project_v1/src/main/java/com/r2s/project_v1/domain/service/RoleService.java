package com.r2s.project_v1.domain.service;

import com.r2s.project_v1.domain.models.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role createRole(Role role);
    Role UpdateRole(Role role);
    Role findById(Integer id);
    Role findByName(String name);
}
