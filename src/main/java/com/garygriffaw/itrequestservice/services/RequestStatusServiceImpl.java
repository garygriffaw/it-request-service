package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.entities.RequestStatus;
import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import com.garygriffaw.itrequestservice.mappers.RequestStatusMapper;
import com.garygriffaw.itrequestservice.model.RequestStatusDTO;
import com.garygriffaw.itrequestservice.repositories.RequestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestStatusServiceImpl implements RequestStatusService {
    private final RequestStatusRepository requestStatusRepository;
    private final RequestStatusMapper requestStatusMapper;

    @Override
    public List<RequestStatusDTO> listRequestStatuses() {
        List<RequestStatus> requestStatuses = requestStatusRepository.findAll();

        return requestStatuses.stream().map(requestStatusMapper::requestStatusToRequestStatusDTO).toList();
    }

    @Override
    public Optional<RequestStatusDTO> getRequestStatusByRequestStatus(RequestStatusEnum requestStatus) {
        return Optional.ofNullable(requestStatusMapper.requestStatusToRequestStatusDTO(requestStatusRepository.findByRequestStatusCode(requestStatus)
                .orElse(null)));
    }
}
