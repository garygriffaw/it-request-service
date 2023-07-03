package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.enums.RoleEnum;
import com.garygriffaw.itrequestservice.model.RoleDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleServiceImpl implements RoleService {

    private Map<Integer, RoleDTO> roleMap;

    public RoleServiceImpl() {
        this.roleMap = new HashMap<>();

        RoleDTO role1 = RoleDTO.builder()
                .id(1)
                .role(RoleEnum.USER)
                .build();
        roleMap.put(role1.getId(), role1);

        RoleDTO role2 = RoleDTO.builder()
                .id(2)
                .role(RoleEnum.ADMIN)
                .build();
        roleMap.put(role2.getId(), role2);
    }

    @Override
    public List<RoleDTO> listRoles() {
        return roleMap.values().stream().toList();
    }
}
