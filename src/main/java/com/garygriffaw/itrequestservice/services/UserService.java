package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> getUserByUserName(String username);
}
