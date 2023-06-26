package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.services.RequestService;
import com.garygriffaw.itrequestservice.services.RequestServiceImpl;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RequestController.class)
@Import(SecurityConfiguration.class)
class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RequestService requestService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @MockBean
    JwtDecoder jwtDecoder;

    @MockBean
    TokenRepository tokenRepository;

    @MockBean
    LogoutHandler logoutHandler;

    RequestServiceImpl requestServiceImpl;

    public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor =
            jwt().jwt(jwt -> {
                jwt.claims(claims -> {
                            claims.put("scope", "message-read");
                            claims.put("scope", "message-write");
                        })
                        .subject("messaging-client")
                        .notBefore(Instant.now().minusSeconds(5l));
            });

    @BeforeEach
    void setUp() {
        requestServiceImpl = new RequestServiceImpl();
    }

    @Test
    void testListMyRequests() throws Exception {
        given(requestService.listRequestsByRequester(any() ,any(), any()))
                .willReturn(requestServiceImpl.listRequestsByRequester("abc",1, 25));

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH)
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    void testGetMyRequestById() throws Exception {
        RequestDTO testRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.getRequestByIdAndRequester(testRequest.getId(), "abc"))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH_ID, testRequest.getId())
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));
    }

    @Test
    void testGetMyRequestByIdNotFound() throws Exception {
        given(requestService.getRequestByIdAndRequester(any(Integer.class), any(String.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH_ID, 9999)
                        .with(user("abc")))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListRequests() throws Exception {
        given(requestService.listRequests(any(), any()))
                .willReturn(requestServiceImpl.listRequests(1, 25));

        mockMvc.perform(get(RequestController.REQUESTS_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testListRequestsForbidden() throws Exception {
        mockMvc.perform(get(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetRequestById() throws Exception {
        RequestDTO testRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.getRequestById(testRequest.getId()))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetRequestByIdNotFound() throws Exception {
        given(requestService.getRequestById(any(Integer.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, 9999))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testGetRequestByIdForbidden() throws Exception {
        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, 9999))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "abc")
    @Test
    void testCreateNewRequest() throws Exception {
        RequestDTO newRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        newRequest.setId(null);
        newRequest.setVersion(null);

        given(requestService.saveNewRequest(any(RequestDTO.class), any()))
                .willReturn(requestServiceImpl.listRequests(1, 25).getContent().get(1));

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @WithMockUser(username = "abc")
    @Test
    void testCreateNewRequestBlankRequest() throws Exception {
        RequestDTO newRequest = RequestDTO.builder().build();

        MvcResult mvcResult = mockMvc.perform(post(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

//    @Test
//    void testUpdateRequest() throws Exception {
//        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);
//
//        given(requestService.updateRequestById(any(), any(RequestDTO.class)))
//                .willReturn(Optional.of(request));
//
//        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, request.getId())
//                        .with(jwtRequestPostProcessor)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNoContent());
//
//        verify(requestService).updateRequestById(any(Integer.class), any(RequestDTO.class));
//    }
//
//    @Test
//    void testUpdateRequestNotFound() throws Exception {
//        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);
//
//        given(requestService.updateRequestById(any(), any(RequestDTO.class)))
//                .willReturn(Optional.empty());
//
//        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, request.getId())
//                        .with(jwtRequestPostProcessor)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound());
//    }
}