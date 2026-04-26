package com.project.accesscontrol.domain.role.service;

import com.project.accesscontrol.common.exception.ResourceNotFoundException;
import com.project.accesscontrol.domain.role.entity.Role;
import com.project.accesscontrol.domain.role.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRequiredRole(String roleName){
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
    }
}
