package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.bootstrap.BootstrapData;
import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.mappers.UserMapper;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.model.RequestRequesterDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
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

import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RequestControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

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

    @WithMockUser(username = BootstrapData.TEST_USER_2)
    @Transactional
    @Test
    void testCreateNewRequest() throws Exception {
        RequestRequesterDTO newRequest = RequestRequesterDTO.builder()
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

    @WithMockUser(username = BootstrapData.TEST_USER_2)
    @Test
    void testCreateNewRequestTitleTooShort() throws Exception {
        RequestRequesterDTO newRequest = RequestRequesterDTO.builder()
                .title("test")
                .description("This is a test of testCreateNewRequest")
                .build();

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @WithMockUser(username = "abc")
    @Transactional
    @Test
    void testCreateNewRequestForbidden() throws Exception {
        RequestRequesterDTO newRequest = RequestRequesterDTO.builder()
                .title("testCreateNewRequest")
                .description("This is a test of testCreateNewRequest")
                .build();

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2, roles = "ADMIN")
    @Rollback
    @Transactional
    @Test
    void testUpdateRequest() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);
        final String updateTitle = getUpdatedString(testRequest.getTitle());
        final String updateDescription = getUpdatedString(testRequest.getDescription());
        final User updateUser = userRepository.findByUsername(BootstrapData.TEST_USER_1).get();
        final UserUnsecureDTO updateUserDTO = userMapper.userToUserUnsecureDTO(updateUser);
        final String updateResolution = getUpdatedString(testRequest.getResolution());
        RequestDTO testDTO = RequestDTO.builder()
                .id(testRequest.getId())
                .version(testRequest.getVersion())
                .title(updateTitle)
                .description(updateDescription)
                .requester(updateUserDTO)
                .resolution(updateResolution)
                .build();

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNoContent());

        Request updatedRequest = requestRepository.findById(testDTO.getId()).get();
        assertThat(updatedRequest.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedRequest.getDescription()).isEqualTo(updateDescription);
        assertThat(updatedRequest.getRequester().getUsername()).isEqualTo(updateUser.getUsername());
        assertThat(updatedRequest.getResolution()).isEqualTo(updateResolution);
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2, roles = "ADMIN")
    @Rollback
    @Transactional
    @Test
    void testUpdateRequestNotFound() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);
        final String updateTitle = getUpdatedString(testRequest.getTitle());
        final String updateDescription = getUpdatedString(testRequest.getDescription());
        final User updateUser = userRepository.findByUsername(BootstrapData.TEST_USER_1).get();
        final UserUnsecureDTO updateUserDTO = userMapper.userToUserUnsecureDTO(updateUser);
        final String updateResolution = getUpdatedString(testRequest.getResolution());
        RequestDTO testDTO = RequestDTO.builder()
                .id(testRequest.getId())
                .version(testRequest.getVersion())
                .title(updateTitle)
                .description(updateDescription)
                .requester(updateUserDTO)
                .resolution(updateResolution)
                .build();

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, 9999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2)
    @Rollback
    @Transactional
    @Test
    void testRequesterUpdateRequest() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);
        final String updateTitle = getUpdatedString(testRequest.getTitle());
        final String updateDescription = getUpdatedString(testRequest.getDescription());
        final UserUnsecureDTO updateUser = userMapper.userToUserUnsecureDTO(userRepository.findByUsername(BootstrapData.TEST_USER_1).get());
        final String updateResolution = getUpdatedString(testRequest.getResolution());
        RequestDTO testDTO = RequestDTO.builder()
                .id(testRequest.getId())
                .version(testRequest.getVersion())
                .title(updateTitle)
                .description(updateDescription)
                .requester(updateUser)
                .resolution(updateResolution)
                .build();

        mockMvc.perform(put(RequestController.REQUESTS_REQUESTER_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNoContent());

        Request updatedRequest = requestRepository.findById(testDTO.getId()).get();
        assertThat(updatedRequest.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedRequest.getDescription()).isEqualTo(updateDescription);

        // These should not be updated
        assertThat(updatedRequest.getRequester()).isEqualTo(testRequest.getRequester());
        assertThat(updatedRequest.getResolution()).isEqualTo(testRequest.getResolution());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2)
    @Test
    void testRequesterUpdateRequestNotFound() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);
        final String updateTitle = getUpdatedString(testRequest.getTitle());
        final String updateDescription = getUpdatedString(testRequest.getDescription());
        RequestRequesterDTO testDTO = RequestRequesterDTO.builder()
                .id(testRequest.getId())
                .version(testRequest.getVersion())
                .title(updateTitle)
                .description(updateDescription)
                .build();

        mockMvc.perform(put(RequestController.REQUESTS_REQUESTER_PATH_ID, 9999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_1)
    @Test
    void testRequesterUpdateRequestWrongRequester() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);
        final String updateTitle = testRequest.getTitle() + " updated";
        final String updateDescription = testRequest.getDescription() + " updated";
        RequestRequesterDTO testDTO = RequestRequesterDTO.builder()
                .id(testRequest.getId())
                .version(testRequest.getVersion())
                .title(updateTitle)
                .description(updateDescription)
                .build();

        mockMvc.perform(put(RequestController.REQUESTS_REQUESTER_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2, roles = "ADMIN")
    @Rollback
    @Transactional
    @Test
    void testDeleteRequest() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);

        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(requestRepository.findById(testRequest.getId()).isEmpty());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2, roles = "ADMIN")
    @Test
    void testDeleteRequestNotFound() throws Exception {
        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, 9999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = BootstrapData.TEST_USER_2, roles = "USER")
    @Test
    void testDeleteRequestForbidden() throws Exception {
        Request testRequest = requestRepository.findAll().get(1);

        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private String getUpdatedString(String string) {
        return string + " updated";
    }
}
