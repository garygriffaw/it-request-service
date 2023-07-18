package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.RequestStatus;
import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {

    Optional<RequestStatus> findByRequestStatus(RequestStatusEnum requestStatus);
}
