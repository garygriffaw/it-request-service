package com.garygriffaw.itrequestservice.model;

import com.garygriffaw.itrequestservice.validation.ValidPassword;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Email must have a value.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @ValidPassword
    private String password;
}
