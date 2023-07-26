package com.garygriffaw.itrequestservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateDTO {
    private Integer id;
    private Integer version;

    @NotBlank(message = "Title must not be blank.")
    @NotNull(message = "Title must have a value.")
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters.")
    private String title;

    @NotBlank(message = "Description must not be blank.")
    @NotNull(message = "Description must have a value.")
    @Size(min = 5, max = 500, message = "Title must be between 5 and 500 characters.")
    private String description;
}
