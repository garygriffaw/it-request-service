package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private Map<String, UserDTO> userMap;

    public UserServiceImpl() {
        this.userMap = new HashMap<>();

        UserDTO user1 = UserDTO.builder()
                .id(1)
                .username("user1")
                .firstname("User 1")
                .lastname("Smith")
                .build();
        userMap.put(user1.getUsername(), user1);

        UserDTO user2 = UserDTO.builder()
                .id(2)
                .username("user2")
                .firstname("User 2")
                .lastname("Jones")
                .build();
        userMap.put(user2.getUsername(), user2);
    }

    @Override
    public Optional<UserDTO> getUserByUserName(String username) {
        return Optional.of(userMap.get(username));
    }
}
