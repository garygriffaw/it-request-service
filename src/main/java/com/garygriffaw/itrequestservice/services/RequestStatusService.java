package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import com.garygriffaw.itrequestservice.model.RequestStatusDTO;

import java.util.List;
import java.util.Optional;

public interface RequestStatusService {
    List<RequestStatusDTO> listRequestStatuses();

    List<RequestStatusDTO> listRequestStatusesForRequester();

    List<RequestStatusDTO> listRequestStatusesForAssignedTo();

    Optional<RequestStatusDTO> getRequestStatusByRequestStatus(RequestStatusEnum requestStatus);
}
