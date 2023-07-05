package com.garygriffaw.itrequestservice.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "Username must not be blank.")
    @NotNull(message = "Username must have a value.")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters.")
    @Column(length = 20)
    private String username;

    @NotBlank(message = "First Name must not be blank.")
    @NotNull(message = "First Name must have a value.")
    @Size(max = 100, message = "First Name must be at most 100 characters.")
    @Column(length = 100)
    private String firstname;

    @NotBlank(message = "Last Name must not be blank.")
    @NotNull(message = "Last Name must have a value.")
    @Size(max = 100, message = "Last Name must be at most 100 characters.")
    @Column(length = 100)
    private String lastname;

    private String email;

    private String password;
}
