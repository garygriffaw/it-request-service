package com.garygriffaw.itrequestservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Transactional
    @Rollback
    @Test
    void testRegister() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password("abcd")
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Rollback
    @Test
    void testRegisterDuplicateUsername() throws Exception {
        RegisterRequest registerRequest1 = RegisterRequest.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password("abcd")
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isOk());

        RegisterRequest registerRequest2 = RegisterRequest.builder()
                .username("test1")
                .firstname("First2")
                .lastname("Last2")
                .email("aaa2@aaa.com")
                .password("wxyz")
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest2)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Transactional
    @Rollback
    @Test
    void testAuthenticate() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password("abcd")
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("test1")
                .password("abcd")
                .build();

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Rollback
    @Test
    void testAuthenticateWrongPassword() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password("abcd")
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("test1")
                .password("abcf")
                .build();

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAuthenticateWrongUsername() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("test9999")
                .password("abcd")
                .build();

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isForbidden());
    }
}