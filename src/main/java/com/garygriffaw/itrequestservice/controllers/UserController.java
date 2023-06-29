package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private static final String BASE_PATH = "/api/v1";

    public static final String USERS_PATH = BASE_PATH + "/users";

    private final UserService userService;

    @GetMapping(USERS_PATH)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserAdminDTO> listUsers(@RequestParam(required = false) Integer pageNumber,
                                        @RequestParam(required = false) Integer pageSize) {
        return userService.listUsers(pageNumber, pageSize);
    }
}
