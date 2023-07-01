package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    Optional<UserUnsecureDTO> getUserByUsernameUnsec(String username);

    Page<UserAdminDTO> listUsers(Integer pageNumber, Integer pageSize);

    Optional<UserAdminDTO> getUserByUsername(String username);

    Optional<UserAdminDTO> updateUserByUsername(String username, UserAdminDTO userDTO);
}
