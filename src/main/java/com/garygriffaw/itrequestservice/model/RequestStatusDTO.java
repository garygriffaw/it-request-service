package com.garygriffaw.itrequestservice.model;

import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestStatusDTO {
    private Integer id;

    private String requestStatus;

    private String requestStatusDisplay;
}
