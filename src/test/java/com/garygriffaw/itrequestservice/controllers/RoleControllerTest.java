package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.services.RoleService;
import com.garygriffaw.itrequestservice.services.RoleServiceImpl;
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

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@Import(SecurityConfiguration.class)
class RoleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RoleService roleService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @MockBean
    TokenRepository tokenRepository;

    @MockBean
    LogoutHandler logoutHandler;

    RoleServiceImpl roleServiceImpl;

    @BeforeEach
    void setUp() {
        roleServiceImpl = new RoleServiceImpl();
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListRoles() throws Exception {
        given(roleService.listRoles())
                .willReturn(roleServiceImpl.listRoles());

        mockMvc.perform(get(RoleController.ROLES_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testListRolesForbidden() throws Exception {
        mockMvc.perform(get(RoleController.ROLES_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}