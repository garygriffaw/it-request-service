package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.model.RoleDTO;
import com.garygriffaw.itrequestservice.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RoleController {

    private static final String BASE_PATH = "/api/v1";
    public static final String ROLES_PATH = BASE_PATH + "/roles";

    private final RoleService roleService;

    @GetMapping(ROLES_PATH)
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleDTO> listRoles() {
        return roleService.listRoles();
    }
}
