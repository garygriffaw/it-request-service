package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.RequestDTO;
import org.springframework.data.domain.Page;

public interface RequestService {

    Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize);
}
