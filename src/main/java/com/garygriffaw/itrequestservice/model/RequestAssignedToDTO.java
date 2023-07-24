package com.garygriffaw.itrequestservice.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestAssignedToDTO {
    private Integer id;
    private Integer version;

    @NotNull(message = "Request Status must have a value.")
    private RequestStatusDTO requestStatus;

    @Size(max = 500, message = "Resolution can be at most 500 characters.")
    private String resolution;
}
