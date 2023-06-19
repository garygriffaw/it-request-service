package com.garygriffaw.itrequestservice.entities;

import com.garygriffaw.itrequestservice.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "users")
@Entity
public class Role {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Builder.Default
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
