package com.garygriffaw.itrequestservice.mappers;

import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userUnsecureDTOToUser(UserUnsecureDTO userDTO);

    UserUnsecureDTO userToUserUnsecureDTO(User user);

    User userAdminDTOToUser(UserAdminDTO userDTO);

    UserAdminDTO userToUserAdminDTO(User user);
}
