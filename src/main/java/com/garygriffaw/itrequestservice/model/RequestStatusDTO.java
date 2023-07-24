package com.garygriffaw.itrequestservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestStatusDTO {
    private Integer id;

    private String requestStatusCode;

    private String requestStatusDisplay;
}
