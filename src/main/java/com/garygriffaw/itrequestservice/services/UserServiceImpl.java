package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private Map<String, UserUnsecureDTO> userUnsecureDTOMap;
    private Map<String, UserAdminDTO> userAdminDTOMap;

    public UserServiceImpl() {
        loadUserUnsecureDTOMap();
        loadUserAdminDTOMap();
    }

    @Override
    public Optional<UserUnsecureDTO> getUserByUsernameUnsec(String username) {
        return Optional.of(userUnsecureDTOMap.get(username));
    }

    @Override
    public Page<UserAdminDTO> listUsers(Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(userAdminDTOMap.values()));
    }

    @Override
    public Optional<UserAdminDTO> getUserByUsername(String username) {
        return Optional.of(userAdminDTOMap.get(username));
    }

    private void loadUserUnsecureDTOMap() {
        this.userUnsecureDTOMap = new HashMap<>();

        UserUnsecureDTO userUnsecureDTO1 = UserUnsecureDTO.builder()
                .id(1)
                .username("user1")
                .firstname("User 1")
                .lastname("Smith")
                .build();
        userUnsecureDTOMap.put(userUnsecureDTO1.getUsername(), userUnsecureDTO1);

        UserUnsecureDTO userUnsecureDTO2 = UserUnsecureDTO.builder()
                .id(2)
                .username("user2")
                .firstname("User 2")
                .lastname("Jones")
                .build();
        userUnsecureDTOMap.put(userUnsecureDTO2.getUsername(), userUnsecureDTO2);
    }

    private void loadUserAdminDTOMap() {
        this.userAdminDTOMap = new HashMap<>();

        UserAdminDTO userAdminDTO1 = UserAdminDTO.builder()
                .id(1)
                .username("user1")
                .firstname("User 1")
                .lastname("Smith")
                .build();
        userAdminDTOMap.put(userAdminDTO1.getUsername(), userAdminDTO1);

        UserAdminDTO userAdminDTO2 = UserAdminDTO.builder()
                .id(2)
                .username("user2")
                .firstname("User 2")
                .lastname("Jones")
                .build();
        userAdminDTOMap.put(userAdminDTO2.getUsername(), userAdminDTO2);
    }
}
