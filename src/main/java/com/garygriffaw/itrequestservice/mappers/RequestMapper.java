package com.garygriffaw.itrequestservice.mappers;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import org.mapstruct.Mapper;

@Mapper(uses = UserMapper.class)
public interface RequestMapper {
    Request requestDTOToRequest(RequestDTO dto);

    RequestDTO requestToRequestDTO(Request request);
}
