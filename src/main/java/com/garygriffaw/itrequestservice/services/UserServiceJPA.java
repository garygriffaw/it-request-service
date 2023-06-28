package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.mappers.UserMapper;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import com.garygriffaw.itrequestservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceJPA implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserUnsecureDTO> getUserByUserName(String username) {
        return Optional.ofNullable(userMapper.userToUserUnsecureDTO(userRepository.findByUsername(username)
                .orElse(null)));
    }
}
