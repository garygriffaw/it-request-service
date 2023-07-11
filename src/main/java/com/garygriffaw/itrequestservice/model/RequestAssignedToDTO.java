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

    @NotBlank(message = "Resolution must not be blank.")
    @NotNull(message = "Resolution must have a value.")
    @Size(min = 5, max = 500, message = "Resolution must be between 5 and 500 characters.")
    @Column(length = 500)
    private String resolution;
}
