package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.entities.Role;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.mappers.UserMapper;
import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.repositories.RoleRepository;
import com.garygriffaw.itrequestservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerIT {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ObjectMapper objectMapper;

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

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListUsers() throws Exception {
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
        User testUser = userRepository.findAll().get(0);

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

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Rollback
    @Transactional
    @Test
    void testUpdateUser() throws Exception {
        User testUser = userRepository.findAll().get(0);

        Set<Role> allRoles = new HashSet<>();
        allRoles.addAll(roleRepository.findAll());

        User updateUser = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .firstname(getUpdatedString(testUser.getFirstname()))
                .lastname(getUpdatedString(testUser.getLastname()))
                .email(getUpdatedString(testUser.getEmail()))
                .roles(allRoles)
                .build();

        UserAdminDTO updateUserDTO = userMapper.userToUserAdminDTO(updateUser);

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, updateUserDTO.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isNoContent());

        User updatedUser = userRepository.findByUsername(updateUser.getUsername()).get();
        assertThat(updatedUser.getId()).isEqualTo(updateUser.getId());
        assertThat(updatedUser.getUsername()).isEqualTo(updateUser.getUsername());
        assertThat(updatedUser.getFirstname()).isEqualTo(updateUser.getFirstname());
        assertThat(updatedUser.getLastname()).isEqualTo(updateUser.getLastname());
        assertThat(updatedUser.getEmail()).isEqualTo(updateUser.getEmail());
        assertThat(updatedUser.getRoles()).isEqualTo(updateUser.getRoles());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Rollback
    @Transactional
    @Test
    void testUpdateUserNotFound() throws Exception {
        User testUser = userRepository.findAll().get(0);

        Set<Role> allRoles = new HashSet<>();
        allRoles.addAll(roleRepository.findAll());

        User updateUser = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .firstname(getUpdatedString(testUser.getFirstname()))
                .lastname(getUpdatedString(testUser.getLastname()))
                .email(getUpdatedString(testUser.getEmail()))
                .roles(allRoles)
                .build();

        UserAdminDTO updateUserDTO = userMapper.userToUserAdminDTO(updateUser);

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, "abc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Rollback
    @Transactional
    @Test
    void testUpdateUserForbidden() throws Exception {
        User testUser = userRepository.findAll().get(0);

        Set<Role> allRoles = new HashSet<>();
        allRoles.addAll(roleRepository.findAll());

        User updateUser = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .firstname(getUpdatedString(testUser.getFirstname()))
                .lastname(getUpdatedString(testUser.getLastname()))
                .email(getUpdatedString(testUser.getEmail()))
                .roles(allRoles)
                .build();

        UserAdminDTO updateUserDTO = userMapper.userToUserAdminDTO(updateUser);

        mockMvc.perform(put(UserController.USERS_PATH_USERNAME, "abc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isForbidden());
    }

    private String getUpdatedString(String string) {
        return "a" + string;
    }
}
