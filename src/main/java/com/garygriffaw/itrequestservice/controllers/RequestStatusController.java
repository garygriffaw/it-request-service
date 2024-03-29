package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.model.RequestStatusDTO;
import com.garygriffaw.itrequestservice.services.RequestStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RequestStatusController {

    private static final String BASE_PATH = "/api/v1";
    public static final String REQUEST_STATUS_PATH = BASE_PATH + "/requeststatuses";
    public static final String REQUEST_STATUS_FOR_REQUESTER_PATH = BASE_PATH + "/requeststatusesforrequester";
    public static final String REQUEST_STATUS_FOR_ASSIGNED_TO_PATH = BASE_PATH + "/requeststatusesforassignedto";

    private final RequestStatusService requestStatusService;

    @GetMapping(REQUEST_STATUS_PATH)
    public List<RequestStatusDTO> listRequestStatuses() {
        return requestStatusService.listRequestStatuses();
    }

    @GetMapping(REQUEST_STATUS_FOR_REQUESTER_PATH)
    public List<RequestStatusDTO> listRequestStatusesForRequester() {
        return requestStatusService.listRequestStatusesForRequester();
    }

    @GetMapping(REQUEST_STATUS_FOR_ASSIGNED_TO_PATH)
    public List<RequestStatusDTO> listRequestStatusesForAssignedTo() {
        return requestStatusService.listRequestStatusesForAssignedTo();
    }
}
