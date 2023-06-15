package com.garygriffaw.itrequestservice.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RequestDTO {
    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String title;

    @NotBlank
    @NotNull
    @Size(max = 500)
    @Column(length = 500)
    private String description;

    private UserDTO requester;

    private String resolution;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
