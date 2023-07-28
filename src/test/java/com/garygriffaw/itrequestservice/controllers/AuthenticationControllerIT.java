package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.model.UserAuthenticationDTO;
import com.garygriffaw.itrequestservice.model.UserRegisterDTO;
import com.garygriffaw.itrequestservice.services.AuthenticationService;
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

    private final String VALID_PASSWORD = "a3gR!ojf";

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
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password(VALID_PASSWORD)
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Rollback
    @Test
    void testRegisterDuplicateUsername() throws Exception {
        UserRegisterDTO userRegisterDTO1 = UserRegisterDTO.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password(VALID_PASSWORD)
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO1)))
                .andExpect(status().isOk());

        UserRegisterDTO userRegisterDTO2 = UserRegisterDTO.builder()
                .username("test1")
                .firstname("First2")
                .lastname("Last2")
                .email("aaa2@aaa.com")
                .password(VALID_PASSWORD + "a")
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO2)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Transactional
    @Rollback
    @Test
    void testAuthenticate() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password(VALID_PASSWORD)
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isOk());

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .username("test1")
                .password(VALID_PASSWORD)
                .build();

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthenticationDTO)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Rollback
    @Test
    void testAuthenticateWrongPassword() throws Exception {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("test1")
                .firstname("First")
                .lastname("Last")
                .email("aaa@aaa.com")
                .password(VALID_PASSWORD)
                .build();

        mockMvc.perform(post(AuthenticationController.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isOk());

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .username("test1")
                .password(VALID_PASSWORD + "a")
                .build();

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthenticationDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAuthenticateWrongUsername() throws Exception {
        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .username("test9999")
                .password(VALID_PASSWORD)
                .build();

        mockMvc.perform(post(AuthenticationController.AUTHENTICATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthenticationDTO)))
                .andExpect(status().isForbidden());
    }
}