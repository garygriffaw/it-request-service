package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private Map<String, UserUnsecureDTO> userMap;

    public UserServiceImpl() {
        this.userMap = new HashMap<>();

        UserUnsecureDTO user1 = UserUnsecureDTO.builder()
                .id(1)
                .username("user1")
                .firstname("User 1")
                .lastname("Smith")
                .build();
        userMap.put(user1.getUsername(), user1);

        UserUnsecureDTO user2 = UserUnsecureDTO.builder()
                .id(2)
                .username("user2")
                .firstname("User 2")
                .lastname("Jones")
                .build();
        userMap.put(user2.getUsername(), user2);
    }

    @Override
    public Optional<UserUnsecureDTO> getUserByUserName(String username) {
        return Optional.of(userMap.get(username));
    }
}
