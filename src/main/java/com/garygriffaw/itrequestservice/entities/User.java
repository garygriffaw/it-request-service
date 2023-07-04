package com.garygriffaw.itrequestservice.entities;

import com.garygriffaw.itrequestservice.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "Username must not be blank.")
    @NotNull(message = "Username must have a value.")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters.")
    @Column(length = 20, unique = true)
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

//    @NotBlank(message = "Password must not be blank.")
//    @NotNull(message = "Password must have a value.")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Builder.Default
    @OneToMany(mappedBy = "requester")
    private Set<Request> requests = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
}
