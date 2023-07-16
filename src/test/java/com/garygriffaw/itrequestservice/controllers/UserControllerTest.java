package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.services.UserService;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Autowired
    ObjectMapper objectMapper;


    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListUsers() throws Exception {
        Page<UserAdminDTO> testPage = new PageImpl<>(new ArrayList<>());

        given(userService.listUsers(any(), any()))
                .willReturn(testPage);

        mockMvc.perform(get(UserController.USERS_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).listUsers(any(), any());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testListUsersForbidden() throws Exception {
        mockMvc.perform(get(UserController.USERS_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).listUsers(any(), any());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetUserByUsername() throws Exception {
        UserAdminDTO testUser = getUserAdminDTO();

        given(userService.getUserByUsername(any()))
                .willReturn(Optional.of(testUser));

        mockMvc.perform(get(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())));

        verify(userService, times(1)).getUserByUsername(any(String.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        given(userService.getUserByUsername(any()))
                .willReturn(Optional.empty());

        mockMvc.perform(get(UserController.USERS_PATH_USERNAME, "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUsername(any(String.class));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testGetUserByUsernameForbidden() throws Exception {
        mockMvc.perform(get(UserController.USERS_PATH_USERNAME, "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).getUserByUsername(any(String.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateUser() throws Exception {
        UserAdminDTO testUser = getUserAdminDTO();

        given(userService.updateUserByUsername(any(), any()))
                .willReturn(Optional.of(testUser));

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUserByUsername(any(String.class), any(UserAdminDTO.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateUserUsernameTooShort() throws Exception {
        UserAdminDTO testUser = getUserAdminDTO();
        testUser.setUsername("abcd");

        given(userService.updateUserByUsername(any(), any()))
                .willReturn(Optional.of(testUser));

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).updateUserByUsername(any(String.class), any(UserAdminDTO.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateUserEmailInvalid() throws Exception {
        UserAdminDTO testUser = getUserAdminDTO();
        testUser.setEmail("aaa");

        given(userService.updateUserByUsername(any(), any()))
                .willReturn(Optional.of(testUser));

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).updateUserByUsername(any(String.class), any(UserAdminDTO.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateUserNotFound() throws Exception {
        UserAdminDTO testUser = getUserAdminDTO();

        given(userService.updateUserByUsername(any(), any()))
                .willReturn(Optional.empty());

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUserByUsername(any(String.class), any(UserAdminDTO.class));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testUpdateUserForbidden() throws Exception {
        UserAdminDTO testUser = getUserAdminDTO();

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, testUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).updateUserByUsername(any(String.class), any(UserAdminDTO.class));
    }

    private UserAdminDTO getUserAdminDTO() {
        return UserAdminDTO.builder()
                .id(1)
                .username("user1")
                .firstname("Joe")
                .lastname("Brown")
                .email("joe@mail.com")
                .build();
    }
}