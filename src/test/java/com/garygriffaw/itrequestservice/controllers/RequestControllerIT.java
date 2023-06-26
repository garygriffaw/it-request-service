package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RequestControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestRepository requestRepository;

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

    @WithMockUser(username = "test_user_2")
    @Test
    void testListMyRequests() throws Exception {
        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @WithMockUser(username = "test_user_2")
    @Test
    void testGetMyRequestById() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));
    }

    @WithMockUser(username = "test_user_2")
    @Test
    void testGetMyRequestByIdNotfound() throws Exception {
            mockMvc.perform(get(RequestController.MY_REQUESTS_PATH_ID, 9999)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListRequests() throws Exception {
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
        Request testRequest = requestRepository.findAll().get(1);

        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetRequestByIdNotfound() throws Exception {
        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, 9999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testGetRequestByIdForbidden() throws Exception {
        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, 9999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "test_user_2")
    @Test
    void testCreateNewRequest() throws Exception {
        RequestDTO newRequest = RequestDTO.builder()
                .title("testCreateNewRequest")
                .description("This is a test of testCreateNewRequest")
                .build();

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
