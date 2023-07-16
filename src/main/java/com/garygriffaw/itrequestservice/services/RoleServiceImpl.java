package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.entities.Role;
import com.garygriffaw.itrequestservice.mappers.RoleMapper;
import com.garygriffaw.itrequestservice.model.RoleDTO;
import com.garygriffaw.itrequestservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDTO> listRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::roleToRoleDTO).toList();
    }
}
