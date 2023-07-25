package com.garygriffaw.itrequestservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.config.SecurityConfiguration;
import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import com.garygriffaw.itrequestservice.model.*;
import com.garygriffaw.itrequestservice.services.RequestService;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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

    @Captor
    ArgumentCaptor<Integer> requestIdArgumentCaptor;


    @Test
    void testListMyRequests() throws Exception {
        Page<RequestDTO> testPage = new PageImpl<>(new ArrayList<>());

        given(requestService.listRequestsByRequester(any() ,any(), any()))
                .willReturn(testPage);

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH)
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(requestService, times(1)).listRequestsByRequester(any(String.class) ,any(), any());
    }

    @Test
    void testListMyRequestsByDescription() throws Exception {
        Page<RequestDTO> testPage = new PageImpl<>(new ArrayList<>());

        given(requestService.listRequestsByRequesterAndDescription(any() ,any() ,any(), any()))
                .willReturn(testPage);

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH)
                        .with(user("abc"))
                        .param("description", "test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(requestService, times(1)).listRequestsByRequesterAndDescription(any(String.class) ,any(String.class), any(), any());
    }

    @Test
    void testGetMyRequestById() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.getRequestByIdAndRequester(testRequest.getId(), "abc"))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH_ID, testRequest.getId())
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));

        verify(requestService, times(1)).getRequestByIdAndRequester(any(Integer.class) ,any(String.class));
    }

    @Test
    void testGetMyRequestByIdNotFound() throws Exception {
        given(requestService.getRequestByIdAndRequester(any(Integer.class), any(String.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.MY_REQUESTS_PATH_ID, 9999)
                        .with(user("abc")))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).getRequestByIdAndRequester(any(Integer.class) ,any(String.class));
    }

    @Test
    void testListAssignedToRequests() throws Exception {
        Page<RequestDTO> testPage = new PageImpl<>(new ArrayList<>());

        given(requestService.listRequestsByAssignedTo(any() ,any(), any()))
                .willReturn(testPage);

        mockMvc.perform(get(RequestController.ASSIGNED_TO_REQUESTS_PATH)
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(requestService, times(1)).listRequestsByAssignedTo(any(String.class) ,any(), any());
    }

    @Test
    void testGetAssignedToRequestById() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.getRequestByIdAndAssignedTo(testRequest.getId(), "abc"))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, testRequest.getId())
                        .with(user("abc"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));

        verify(requestService, times(1)).getRequestByIdAndAssignedTo(any(Integer.class) ,any(String.class));
    }

    @Test
    void testGetAssignedToRequestByIdNotFound() throws Exception {
        given(requestService.getRequestByIdAndAssignedTo(any(Integer.class), any(String.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, 9999)
                        .with(user("abc")))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).getRequestByIdAndAssignedTo(any(Integer.class) ,any(String.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListRequests() throws Exception {
        Page<RequestDTO> testPage = new PageImpl<>(new ArrayList<>());

        given(requestService.listRequests(any(), any()))
                .willReturn(testPage);

        mockMvc.perform(get(RequestController.REQUESTS_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(requestService, times(1)).listRequests(any() ,any());
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testListRequestsForbidden() throws Exception {
        mockMvc.perform(get(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(requestService, times(0)).listRequests(any() ,any());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testListRequestsByDescriptionContainingIgnoreCase() throws Exception {
        Page<RequestDTO> testPage = new PageImpl<>(new ArrayList<>());

        given(requestService.listRequestsByDescriptionContainingIgnoreCase(any(), any(), any()))
                .willReturn(testPage);

        mockMvc.perform(get(RequestController.REQUESTS_PATH)
                        .param("description", "software")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(requestService, times(1)).listRequestsByDescriptionContainingIgnoreCase(any(String.class) ,any(), any());
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetRequestById() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.getRequestById(testRequest.getId()))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRequest.getId())))
                .andExpect(jsonPath("$.title", is(testRequest.getTitle())));

        verify(requestService, times(1)).getRequestById(any(Integer.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testGetRequestByIdNotFound() throws Exception {
        given(requestService.getRequestById(any(Integer.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, 9999))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).getRequestById(any(Integer.class));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testGetRequestByIdForbidden() throws Exception {
        mockMvc.perform(get(RequestController.REQUESTS_PATH_ID, 9999))
                .andExpect(status().isForbidden());

        verify(requestService, times(0)).getRequestById(any(Integer.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testCreateNewRequest() throws Exception {
        RequestRequesterDTO newRequest = getTestRequestRequesterDTO();

        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.saveNewRequest(any(RequestRequesterDTO.class), any()))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        verify(requestService, times(1)).saveNewRequest(any(RequestRequesterDTO.class), any(String.class));
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
                .andExpect(jsonPath("$.length()", is(5)))
                .andReturn();

        verify(requestService, times(0)).saveNewRequest(any(RequestRequesterDTO.class), any(String.class));
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @WithMockUser(username = "abc")
    @Test
    void testCreateNewRequestForbidden() throws Exception {
        RequestRequesterDTO newRequest = getTestRequestRequesterDTO();

        given(requestService.saveNewRequest(any(RequestRequesterDTO.class), any()))
                .willReturn(Optional.empty());

        mockMvc.perform(post(RequestController.REQUESTS_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isForbidden());

        verify(requestService, times(1)).saveNewRequest(any(RequestRequesterDTO.class), any(String.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateRequest() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.updateRequestById(any(), any()))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).updateRequestById(any(Integer.class), any(RequestDTO.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testUpdateRequestNotFound() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.updateRequestById(any(), any()))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).updateRequestById(any(Integer.class), any(RequestDTO.class));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testUpdateRequestForbidden() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        mockMvc.perform(put(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isForbidden());

        verify(requestService, times(0)).updateRequestById(any(Integer.class), any(RequestDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testRequesterUpdateRequest() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.updateRequestByIdAndRequester(any(), any(), any(RequestRequesterDTO.class)))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(put(RequestController.MY_REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).updateRequestByIdAndRequester(any(Integer.class), any(String.class), any(RequestRequesterDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testRequesterUpdateRequestNotFound() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.updateRequestByIdAndRequester(any(), any(), any(RequestRequesterDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.MY_REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).updateRequestByIdAndRequester(any(Integer.class), any(String.class), any(RequestRequesterDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testAssignedToUpdateRequest() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.updateRequestByIdAndAssignedTo(any(), any(), any(RequestAssignedToDTO.class)))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(put(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).updateRequestByIdAndAssignedTo(any(Integer.class), any(String.class), any(RequestAssignedToDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testAssignedToUpdateRequestNotFound() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.updateRequestByIdAndAssignedTo(any(), any(), any(RequestAssignedToDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).updateRequestByIdAndAssignedTo(any(Integer.class), any(String.class), any(RequestAssignedToDTO.class));
    }

    @WithMockUser(username = "abc")
    @Test
    void testAssignedToUpdateRequestResolutionTooShort() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();
        RequestAssignedToDTO testUpdateRequest = RequestAssignedToDTO.builder()
                .id(testRequest.getId())
                .version(testRequest.getVersion())
                .resolution("aaaa")
                .build();

        given(requestService.updateRequestByIdAndAssignedTo(any(), any(), any(RequestAssignedToDTO.class)))
                .willReturn(Optional.of(testRequest));

        mockMvc.perform(put(RequestController.ASSIGNED_TO_REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isBadRequest());

        verify(requestService, times(0)).updateRequestByIdAndAssignedTo(any(Integer.class), any(String.class), any(RequestAssignedToDTO.class));
    }

    @WithMockUser(username = "abc", roles = "ADMIN")
    @Test
    void testDeleteRequest() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        given(requestService.deleteById(any()))
                .willReturn(true);

        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(requestService).deleteById(requestIdArgumentCaptor.capture());

        assertThat(testRequest.getId()).isEqualTo(requestIdArgumentCaptor.getValue());

        verify(requestService, times(1)).deleteById(any(Integer.class));
    }

    @WithMockUser(username = "abc", roles = "USER")
    @Test
    void testDeleteRequestForbidden() throws Exception {
        RequestDTO testRequest = getTestRequestDTO();

        mockMvc.perform(delete(RequestController.REQUESTS_PATH_ID, testRequest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(requestService, times(0)).deleteById(any(Integer.class));
    }

    private RequestRequesterDTO getTestRequestRequesterDTO() {
        RequestStatusDTO requestStatus = RequestStatusDTO.builder()
                .id(1)
                .requestStatusCode(RequestStatusEnum.CREATED.name())
                .build();

        return RequestRequesterDTO.builder()
                .id(1)
                .version(0)
                .title("Test title")
                .description("Test description")
                .requestStatus(requestStatus)
                .resolution("Test resolution")
                .build();
    }

    private RequestDTO getTestRequestDTO() {
        UserUnsecureDTO requester = UserUnsecureDTO.builder()
                .id(1)
                .username("user1")
                .firstname("Bob")
                .lastname("Smith")
                .email("bob@mail.com")
                .build();

        UserUnsecureDTO assignedTo = UserUnsecureDTO.builder()
                .id(2)
                .username("user2")
                .firstname("Jane")
                .lastname("Johnson")
                .email("jane@mail.com")
                .build();

        RequestStatusDTO requestStatus = RequestStatusDTO.builder()
                .id(1)
                .requestStatusCode(RequestStatusEnum.CREATED.name())
                .build();

        return RequestDTO.builder()
                .id(1)
                .version(0)
                .title("Test title")
                .description("Test description")
                .requester(requester)
                .requestStatus(requestStatus)
                .assignedTo(assignedTo)
                .resolution("Test resolution")
                .build();
    }
}