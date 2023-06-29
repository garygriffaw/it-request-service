package com.garygriffaw.itrequestservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserAdminDTO {
    private Integer id;

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

//    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
//            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleDTO> roles = new HashSet<>();
}
