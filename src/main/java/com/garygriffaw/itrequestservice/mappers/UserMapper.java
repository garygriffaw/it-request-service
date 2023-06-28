package com.garygriffaw.itrequestservice.mappers;

import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userUnsecureDTOToUser(UserUnsecureDTO userDTO);

    UserUnsecureDTO userToUserUnsecureDTO(User user);
}
