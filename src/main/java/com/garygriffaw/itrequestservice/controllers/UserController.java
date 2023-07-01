package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private static final String BASE_PATH = "/api/v1";

    public static final String USERS_PATH = BASE_PATH + "/users";
    public static final String USERS_PATH_USERNAME = USERS_PATH + "/{username}";


    private final UserService userService;

    @GetMapping(USERS_PATH)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserAdminDTO> listUsers(@RequestParam(required = false) Integer pageNumber,
                                        @RequestParam(required = false) Integer pageSize) {
        return userService.listUsers(pageNumber, pageSize);
    }

    @GetMapping(USERS_PATH_USERNAME)
    @PreAuthorize("hasRole('ADMIN')")
    public UserAdminDTO getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username).orElseThrow(NotFoundException::new);
    }

    @PutMapping(USERS_PATH_USERNAME)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateUserByUsername(@PathVariable("username") String username,
                                               @Validated @RequestBody UserAdminDTO userDTO) {
        if (userService.updateUserByUsername(username, userDTO).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
