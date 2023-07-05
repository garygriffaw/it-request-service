package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.model.UserAuthenticationResponseDTO;
import com.garygriffaw.itrequestservice.model.UserAuthenticationDTO;
import com.garygriffaw.itrequestservice.model.UserRegisterDTO;
import com.garygriffaw.itrequestservice.services.AuthenticationService;
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
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email("a@a.aa")
                .build();

        UserAuthenticationResponseDTO authenticationResponse = UserAuthenticationResponseDTO.builder()
                .build();

        given(authenticationService.register(any(UserRegisterDTO.class)))
                .willReturn(authenticationResponse);

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUsernameTooShort() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcd")
                .firstname("a")
                .lastname("b")
                .email("a@a.aa")
                .build();

        UserAuthenticationResponseDTO authenticationResponse = UserAuthenticationResponseDTO.builder()
                .build();

        given(authenticationService.register(any(UserRegisterDTO.class)))
                .willReturn(authenticationResponse);

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterInvalidEmail() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email("a@a")
                .build();

        UserAuthenticationResponseDTO authenticationResponse = UserAuthenticationResponseDTO.builder()
                .build();

        given(authenticationService.register(any(UserRegisterDTO.class)))
                .willReturn(authenticationResponse);

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAuthenticate() throws Exception {
        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .build();

        UserAuthenticationResponseDTO authenticationResponse = UserAuthenticationResponseDTO.builder()
                .build();

        given(authenticationService.authenticate(any(UserAuthenticationDTO.class)))
                .willReturn(authenticationResponse);

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthenticationDTO)))
                .andExpect(status().isOk());
    }
}