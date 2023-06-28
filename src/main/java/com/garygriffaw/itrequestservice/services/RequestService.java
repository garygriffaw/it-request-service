package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.model.RequestRequesterDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RequestService {

    Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize);

    Page<RequestDTO> listRequestsByRequester(String requesterUsername, Integer pageNumber, Integer pageSize);

    Optional<RequestDTO> getRequestById(Integer requestId);

    Optional<RequestDTO> getRequestByIdAndRequester(Integer requestId, String requesterUsername);

    RequestDTO saveNewRequest(RequestDTO requestDTO, String requesterUsername);

    Optional<RequestDTO> updateRequestById(Integer requestId, RequestDTO requestDTO);

    Optional<RequestDTO> updateRequestByIdAndRequester(Integer requestId, String requesterUsername, RequestRequesterDTO requestDTO);

    boolean deleteById(Integer requestId);
}
