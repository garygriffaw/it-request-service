package com.garygriffaw.itrequestservice.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDTO {
    private Integer id;
    private Integer version;

    @NotBlank(message = "Title must not be blank.")
    @NotNull(message = "Title must have a value.")
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters.")
    @Column(length = 50)
    private String title;

    @NotBlank(message = "Description must not be blank.")
    @NotNull(message = "Description must have a value.")
    @Size(min = 5, max = 500, message = "Title must be between 5 and 500 characters.")
    @Column(length = 500)
    private String description;

    private UserDTO requester;

    private String resolution;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
