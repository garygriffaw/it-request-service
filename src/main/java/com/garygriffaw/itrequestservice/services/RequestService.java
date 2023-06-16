package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.RequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface RequestService {

    Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize);

    Optional<RequestDTO> getRequestById(UUID requestId);

    RequestDTO saveNewRequest(RequestDTO requestDTO, HttpServletRequest httpRequest);

    Optional<RequestDTO> updateRequestById(UUID requestId, RequestDTO requestDTO);
}
