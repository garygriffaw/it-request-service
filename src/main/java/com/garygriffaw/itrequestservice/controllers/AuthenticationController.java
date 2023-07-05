package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.services.AuthenticationService;
import com.garygriffaw.itrequestservice.model.UserAuthenticationResponseDTO;
import com.garygriffaw.itrequestservice.model.UserAuthenticationDTO;
import com.garygriffaw.itrequestservice.model.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String BASE_PATH = "/api/v1";
    private static final String AUTH_BASE_PATH = BASE_PATH + "/auth";

    public static final String REGISTER = AUTH_BASE_PATH + "/register";
    public static final String AUTHENTICATE = AUTH_BASE_PATH + "/authenticate";

    private final AuthenticationService authenticationService;

    @PostMapping(REGISTER)
    public ResponseEntity<UserAuthenticationResponseDTO> register(@Validated @RequestBody UserRegisterDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping(AUTHENTICATE)
    public ResponseEntity<UserAuthenticationResponseDTO> authenticate(@RequestBody UserAuthenticationDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
