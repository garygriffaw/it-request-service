package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {

    Page<Request> findAllByRequester(User requester, Pageable pageable);
}
