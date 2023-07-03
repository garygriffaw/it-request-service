package com.garygriffaw.itrequestservice.mappers;

import com.garygriffaw.itrequestservice.entities.Role;
import com.garygriffaw.itrequestservice.model.RoleDTO;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    Role roleDTOToRole(RoleDTO roleDTO);

    RoleDTO roleToRoleDTO(Role role);
}
