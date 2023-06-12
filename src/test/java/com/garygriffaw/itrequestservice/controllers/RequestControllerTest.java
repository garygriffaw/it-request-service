package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.services.RequestService;
import com.garygriffaw.itrequestservice.services.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RequestService requestService;

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
    void testListRequests() throws Exception {
        given(requestService.listRequests(any(), any()))
                .willReturn(requestServiceImpl.listRequests(1, 25));

        mockMvc.perform(get(RequestController.REQUEST_PATH)
                    .with(jwtRequestPostProcessor)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    void testGetRequestById() throws Exception {
        RequestDTO testRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.getRequestById(testRequest.getId()))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.REQUEST_PATH_ID, testRequest.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId().toString())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));
    }

    @Test
    void testGetRequestByIdNotFound() throws Exception {
        given(requestService.getRequestById(any(UUID.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.REQUEST_PATH_ID, UUID.randomUUID())
                    .with(jwtRequestPostProcessor))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewRequest() throws Exception {
        RequestDTO newRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        newRequest.setId(null);
        newRequest.setVersion(null);

        given(requestService.saveNewRequest(any(RequestDTO.class)))
                .willReturn(requestServiceImpl.listRequests(1, 25).getContent().get(1));

        mockMvc.perform(post(RequestController.REQUEST_PATH)
                    .with(jwtRequestPostProcessor)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateRequest() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.updateRequestById(any(), any(RequestDTO.class)))
                .willReturn(Optional.of(request));

        mockMvc.perform(put(RequestController.REQUEST_PATH_ID, request.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(requestService).updateRequestById(any(UUID.class), any(RequestDTO.class));
    }

    @Test
    void testUpdateRequestNotFound() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.updateRequestById(any(), any(RequestDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.REQUEST_PATH_ID, request.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}