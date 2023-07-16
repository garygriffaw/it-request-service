package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.model.RoleDTO;
import com.garygriffaw.itrequestservice.services.RoleService;
import com.garygriffaw.itrequestservice.token.TokenRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListRoles() throws Exception {
        List<RoleDTO> testList = new ArrayList<>();

        given(roleService.listRoles())
                .willReturn(testList);

        mockMvc.perform(get(RoleController.ROLES_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(roleService, times(1)).listRoles();
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testListRolesForbidden() throws Exception {
        mockMvc.perform(get(RoleController.ROLES_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(roleService, times(0)).listRoles();
    }
}