package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserUnsecureDTO> getUserByUserName(String username);
}
