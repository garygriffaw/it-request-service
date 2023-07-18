package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.RequestStatusDTO;

import java.util.List;

public interface RequestStatusService {
    List<RequestStatusDTO> listRequestStatuses();
}
