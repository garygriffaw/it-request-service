package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.services.UserService;
import com.garygriffaw.itrequestservice.services.UserServiceImpl;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @MockBean
    TokenRepository tokenRepository;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @MockBean
    LogoutHandler logoutHandler;


    UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl();
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListUsers() throws Exception {
        given(userService.listUsers(any(), any()))
                .willReturn(userServiceImpl.listUsers(1, 25));

        mockMvc.perform(get(UserController.USERS_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testListUsersForbidden() throws Exception {
        mockMvc.perform(get(UserController.USERS_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetUserByUsername() throws Exception {
        UserAdminDTO testUser = userServiceImpl.listUsers(1, 25).getContent().get(0);

        given(userService.getUserByUsername(testUser.getUsername()))
                .willReturn(Optional.of(testUser));

        mockMvc.perform(get(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        mockMvc.perform(get(UserController.USERS_PATH_USERNAME, "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testGetUserByUsernameForbidden() throws Exception {
        mockMvc.perform(get(UserController.USERS_PATH_USERNAME, "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}