package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RequestController {

    public static final String REQUEST_PATH ="/api/v1/request";
    public static final String REQUEST_PATH_ID = REQUEST_PATH + "/{requestId}";

    private final RequestService requestService;

    @GetMapping(REQUEST_PATH)
    public Page<RequestDTO> listRequests(@RequestParam(required = false) Integer pageNumber,
                                         @RequestParam(required = false) Integer pageSize) {
        return requestService.listRequests(pageNumber, pageSize);
    }

    @GetMapping(REQUEST_PATH_ID)
    public RequestDTO getRequestById(@PathVariable("requestId")UUID requestId) {
        return requestService.getRequestById(requestId).orElseThrow(NotFoundException::new);
    }
}
