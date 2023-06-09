package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Page<Request> findByRequester(User requester, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.requester = :requester AND lower(r.description) like lower(:description)")
    Page<Request> findByRequesterAndDescription(@Param("requester") User requester,
                                                @Param("description") String descriptionLike, Pageable pageable);

    Page<Request> findByAssignedTo(User assignedTo, Pageable pageable);

    Page<Request> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    Optional<Request> findByIdAndRequester(Integer id, User requester);

    Optional<Request> findByIdAndAssignedTo(Integer id, User assignedTo);
}
