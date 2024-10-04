package com.r2s.project_v1.infrastructure.service;

import com.r2s.project_v1.domain.models.Role;
import com.r2s.project_v1.domain.repository.RoleRepository;
import com.r2s.project_v1.domain.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role createRole(Role role) {
        role.setId(getGenerationId());
        return roleRepository.save(role);
    }

    @Override
    public Role UpdateRole(Role role) {
        Role roleFind=findById(role.getId());

        return roleRepository.save(role);
    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id).orElseThrow();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow();
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        // Use most significant bits and ensure it's within the integer range
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
