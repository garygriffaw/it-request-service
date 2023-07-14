package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.exceptions.ForbiddenException;
import com.garygriffaw.itrequestservice.exceptions.NotFoundException;
import com.garygriffaw.itrequestservice.model.RequestAssignedToDTO;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.model.RequestRequesterDTO;
import com.garygriffaw.itrequestservice.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RequestController {

    private static final String BASE_PATH = "/api/v1";

    public static final String REQUESTS_PATH = BASE_PATH + "/requests";
    public static final String REQUESTS_PATH_ID = REQUESTS_PATH + "/{requestId}";

    public static final String MY_REQUESTS_PATH = BASE_PATH + "/myrequests";
    public static final String MY_REQUESTS_PATH_ID = MY_REQUESTS_PATH + "/{requestId}";

    public static final String ASSIGNED_TO_REQUESTS_PATH = BASE_PATH + "/assignedtorequests";
    public static final String ASSIGNED_TO_REQUESTS_PATH_ID = ASSIGNED_TO_REQUESTS_PATH + "/{requestId}";


    private final RequestService requestService;

    @GetMapping(MY_REQUESTS_PATH)
    public Page<RequestDTO> listMyRequests(@RequestParam(required = false) Integer pageNumber,
                                           @RequestParam(required = false) Integer pageSize,
                                           Authentication authentication,
                                           @RequestParam(required = false) String description) {
        if (description != null)
            return requestService.listRequestsByRequesterAndDescription(authentication.getName(), description,
                    pageNumber, pageSize);
        else
            return requestService.listRequestsByRequester(authentication.getName(), pageNumber, pageSize);
    }

    @GetMapping(MY_REQUESTS_PATH_ID)
    public RequestDTO getMyRequestById(@PathVariable("requestId") Integer requestId,
                                       Authentication authentication) {
        return requestService.getRequestByIdAndRequester(requestId, authentication.getName())
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping(ASSIGNED_TO_REQUESTS_PATH)
    public Page<RequestDTO> listAssignedToRequests(@RequestParam(required = false) Integer pageNumber,
                                           @RequestParam(required = false) Integer pageSize,
                                           Authentication authentication) {
        return requestService.listRequestsByAssignedTo(authentication.getName(), pageNumber, pageSize);
    }

    @GetMapping(ASSIGNED_TO_REQUESTS_PATH_ID)
    public RequestDTO getAssignedToRequestById(@PathVariable("requestId") Integer requestId,
                                       Authentication authentication) {
        return requestService.getRequestByIdAndAssignedTo(requestId, authentication.getName())
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping(REQUESTS_PATH)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RequestDTO> listRequests(@RequestParam(required = false) Integer pageNumber,
                                         @RequestParam(required = false) Integer pageSize,
                                         @RequestParam(required = false) String description) {
        if (description != null)
            return requestService.listRequestsByDescriptionContainingIgnoreCase(description, pageNumber, pageSize);
        else
            return requestService.listRequests(pageNumber, pageSize);
    }

    @GetMapping(REQUESTS_PATH_ID)
    @PreAuthorize("hasRole('ADMIN')")
    public RequestDTO getRequestById(@PathVariable("requestId") Integer requestId) {
        return requestService.getRequestById(requestId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(REQUESTS_PATH)
    public ResponseEntity saveNewRequest(@Validated @RequestBody RequestRequesterDTO requestDTO, Authentication authentication) {
        RequestDTO savedRequest = requestService.saveNewRequest(requestDTO, authentication.getName())
                .orElseThrow(ForbiddenException::new);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", REQUESTS_PATH + "/" + savedRequest.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(REQUESTS_PATH_ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateRequestById(@PathVariable("requestId") Integer requestId,
                                            @Validated @RequestBody RequestDTO requestDTO) {
        if (requestService.updateRequestById(requestId, requestDTO).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(MY_REQUESTS_PATH_ID)
    public ResponseEntity updateRequestByIdAndRequester(@PathVariable("requestId") Integer requestId,
                                                        @Validated @RequestBody RequestRequesterDTO requestDTO,
                                                        Authentication authentication) {
        if (requestService.updateRequestByIdAndRequester(requestId, authentication.getName(), requestDTO).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(ASSIGNED_TO_REQUESTS_PATH_ID)
    public ResponseEntity updateRequestByIdAndAssignedTo(@PathVariable("requestId") Integer requestId,
                                                        @Validated @RequestBody RequestAssignedToDTO requestDTO,
                                                        Authentication authentication) {
        if (requestService.updateRequestByIdAndAssignedTo(requestId, authentication.getName(), requestDTO).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(REQUESTS_PATH_ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteById(@PathVariable("requestId") Integer requestId) {
        if (!requestService.deleteById(requestId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
