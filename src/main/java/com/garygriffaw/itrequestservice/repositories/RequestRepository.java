package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Page<Request> findAllByRequester(User requester, Pageable pageable);

    Optional<Request> findByIdAndRequester(Integer id, User requester);
}
