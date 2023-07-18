package com.garygriffaw.itrequestservice.mappers;

import com.garygriffaw.itrequestservice.entities.RequestStatus;
import com.garygriffaw.itrequestservice.model.RequestStatusDTO;
import org.mapstruct.Mapper;

@Mapper
public interface RequestStatusMapper {
    RequestStatus requestStatusDTOToRequestStatus(RequestStatusDTO dto);

    RequestStatusDTO requestStatusToRequestStatusDTO(RequestStatus requestStatus);
}
