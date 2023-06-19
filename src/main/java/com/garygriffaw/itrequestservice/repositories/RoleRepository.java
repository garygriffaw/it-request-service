package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.Role;
import com.garygriffaw.itrequestservice.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRole(RoleEnum role);
}
