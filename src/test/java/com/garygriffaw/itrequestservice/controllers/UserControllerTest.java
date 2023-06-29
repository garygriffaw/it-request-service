package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.services.UserService;
import com.garygriffaw.itrequestservice.services.UserServiceImpl;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @MockBean
    TokenRepository tokenRepository;


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
}