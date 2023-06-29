package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    Optional<UserUnsecureDTO> getUserByUserName(String username);

    Page<UserAdminDTO> listUsers(Integer pageNumber, Integer pageSize);
}
