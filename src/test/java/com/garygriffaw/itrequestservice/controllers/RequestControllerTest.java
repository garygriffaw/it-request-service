package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.model.RequestAssignedToDTO;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.model.RequestRequesterDTO;
import com.garygriffaw.itrequestservice.services.RequestService;
import com.garygriffaw.itrequestservice.services.RequestServiceImpl;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
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
    TokenRepository tokenRepository;

    @MockBean
    LogoutHandler logoutHandler;

    RequestServiceImpl requestServiceImpl;

    @Captor
    ArgumentCaptor<Integer> requestIdArgumentCaptor;


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

    @Test
    void testListAssignedToRequests() throws Exception {
        given(requestService.listRequestsByAssignedTo(any() ,any(), any()))
                .willReturn(requestServiceImpl.listRequestsByAssignedTo("abc",1, 25));

        mockMvc.perform(get(RequestController.ASSIGNED_TO_REQUESTS_PATH)
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    void testGetAssignedToRequestById() throws Exception {
        RequestDTO testRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.getRequestByIdAndAssignedTo(testRequest.getId(), "abc"))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, testRequest.getId())
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));
    }

    @Test
    void testGetAssignedToRequestByIdNotFound() throws Exception {
        given(requestService.getRequestByIdAndAssignedTo(any(Integer.class), any(String.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, 9999)
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
                .andExpect(jsonPath("$.content.length()", is(4)));
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
    void testListRequestsByDescriptionContainingIgnoreCase() throws Exception {
        given(requestService.listRequestsByDescriptionContainingIgnoreCase(any(), any(), any()))
                .willReturn(requestServiceImpl.listRequestsByDescriptionContainingIgnoreCase("software", 1, 25));

        mockMvc.perform(get(RequestController.REQUESTS_PATH)
                        .param("description", "software")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(1)));
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

        given(requestService.saveNewRequest(any(RequestRequesterDTO.class), any()))
                .willReturn(Optional.of(requestServiceImpl.listRequests(1, 25).getContent().get(1)));

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

    @WithMockUser(username = "abc")
    @Test
    void testCreateNewRequestForbidden() throws Exception {
        RequestDTO newRequest = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        newRequest.setId(null);
        newRequest.setVersion(null);

        given(requestService.saveNewRequest(any(RequestRequesterDTO.class), any()))
                .willReturn(Optional.empty());

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateRequest() throws Exception {
        RequestDTO testDTO = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.updateRequestById(any(), any()))
                .willReturn(Optional.of(testDTO));

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNoContent());

        verify(requestService).updateRequestById(any(Integer.class), any(RequestDTO.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateRequestNotFound() throws Exception {
        RequestDTO testDTO = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.updateRequestById(any(), any()))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testUpdateRequestForbidden() throws Exception {
        RequestDTO testDTO = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "abc")
    @Test
    void testRequesterUpdateRequest() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        RequestRequesterDTO testDTO = RequestRequesterDTO.builder()
                .id(request.getId())
                .version(request.getVersion())
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        given(requestService.updateRequestByIdAndRequester(any(), any(), any(RequestRequesterDTO.class)))
                .willReturn(Optional.of(request));

        mockMvc.perform(put(RequestController.MY_REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNoContent());

        verify(requestService).updateRequestByIdAndRequester(any(Integer.class), any(String.class), any(RequestRequesterDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testRequesterUpdateRequestNotFound() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.updateRequestByIdAndRequester(any(), any(), any(RequestRequesterDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.MY_REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc")
    @Test
    void testAssignedToUpdateRequest() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        RequestAssignedToDTO testDTO = RequestAssignedToDTO.builder()
                .id(request.getId())
                .version(request.getVersion())
                .resolution("Resolution updated")
                .build();

        given(requestService.updateRequestByIdAndAssignedTo(any(), any(), any(RequestAssignedToDTO.class)))
                .willReturn(Optional.of(request));

        mockMvc.perform(put(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNoContent());

        verify(requestService).updateRequestByIdAndAssignedTo(any(Integer.class), any(String.class), any(RequestAssignedToDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testAssignedToUpdateRequestNotFound() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        RequestAssignedToDTO testDTO = RequestAssignedToDTO.builder()
                .id(request.getId())
                .version(request.getVersion())
                .resolution("Resolution updated")
                .build();

        given(requestService.updateRequestByIdAndAssignedTo(any(), any(), any(RequestAssignedToDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "abc")
    @Test
    void testAssignedToUpdateRequestResolutionTooShort() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);
        RequestAssignedToDTO testDTO = RequestAssignedToDTO.builder()
                .id(request.getId())
                .version(request.getVersion())
                .resolution("aaaa")
                .build();

        given(requestService.updateRequestByIdAndAssignedTo(any(), any(), any(RequestAssignedToDTO.class)))
                .willReturn(Optional.of(request));

        mockMvc.perform(put(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testDeleteRequest() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        given(requestService.deleteById(any()))
                .willReturn(true);

        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(requestService).deleteById(requestIdArgumentCaptor.capture());

        assertThat(request.getId()).isEqualTo(requestIdArgumentCaptor.getValue());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testDeleteRequestForbidden() throws Exception {
        RequestDTO request = requestServiceImpl.listRequests(1, 25).getContent().get(0);

        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, request.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}