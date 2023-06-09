package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
}
