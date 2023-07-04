package com.garygriffaw.itrequestservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @MockBean
    TokenRepository tokenRepository;

    @MockBean
    LogoutHandler logoutHandler;

    @Test
    void testRegister() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .build();

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .build();

        given(authenticationService.register(any(RegisterRequest.class)))
                .willReturn(authenticationResponse);

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticate() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .build();

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .build();

        given(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .willReturn(authenticationResponse);

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());
    }
}