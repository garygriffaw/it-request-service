package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
}
