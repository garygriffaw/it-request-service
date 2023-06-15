package com.garygriffaw.itrequestservice.mappers;

import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);
}
