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

    private final String VALID_EMAIL = "a@a.aa";
    private final String VALID_PASSWORD = "a3gR!ojf";

    @Test
    void testRegister() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
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
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
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
                .password(VALID_PASSWORD)
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
    void testRegisterPasswordTooShort() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("a7_f3U2")
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
    void testRegisterPasswordNoSpecialCharacter() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("a7pf3U29")
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
    void testRegisterPasswordNoUppercase() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD.toLowerCase())
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
    void testRegisterPasswordNoLowercase() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD.toUpperCase())
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
    void testRegisterPasswordNoDigit() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("aup_EUup")
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
    void testRegisterPasswordAlphabeticalSequence() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("Rdefgh!1")
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
    void testRegisterPasswordNumericalSequence() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("R56789_w")
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
    void testRegisterPasswordQwertySequence() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("Rrtyui_w")
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
    void testRegisterPasswordWhitespace() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("abcde")
                .firstname("a")
                .lastname("b")
                .email(VALID_EMAIL)
                .password("Rr5y i_w")
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