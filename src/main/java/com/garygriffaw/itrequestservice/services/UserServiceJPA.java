package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.mappers.UserMapper;
import com.garygriffaw.itrequestservice.model.UserAdminDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import com.garygriffaw.itrequestservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceJPA implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int MAX_PAGE_SIZE = 1000;

    @Override
    public Optional<UserUnsecureDTO> getUserByUsernameUnsec(String username) {
        return Optional.ofNullable(userMapper.userToUserUnsecureDTO(userRepository.findByUsername(username)
                .orElse(null)));
    }

    @Override
    public Page<UserAdminDTO> listUsers(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<User> userPage;

        userPage = userRepository.findAll(pageRequest);

        return userPage.map(userMapper::userToUserAdminDTO);
    }

    @Override
    public Optional<UserAdminDTO> getUserByUsername(String username) {
        return Optional.ofNullable(userMapper.userToUserAdminDTO(userRepository.findByUsername(username)
                .orElse(null)));
    }

    @Override
    public Optional<UserAdminDTO> updateUserByUsername(String username, UserAdminDTO userDTO) {
        AtomicReference<Optional<UserAdminDTO>> atomicReference = new AtomicReference<>();

        User user = userMapper.userAdminDTOToUser(userDTO);

        userRepository.findByUsername(username).ifPresentOrElse(foundUser -> {
            foundUser.setFirstname(user.getFirstname());
            foundUser.setLastname(user.getLastname());
            foundUser.setEmail(user.getEmail());
            foundUser.setRoles(user.getRoles());
            atomicReference.set(Optional.of(userMapper.userToUserAdminDTO(userRepository.save(foundUser))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = getPageNumber(pageNumber);
        int queryPageSize = getPageSize(pageSize);

        Sort sort = Sort.by(Sort.Order.asc("username"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Integer getPageNumber(Integer pageNumber) {
        if (pageNumber != null && pageNumber > 0)
            return pageNumber - 1;

        return DEFAULT_PAGE;
    }

    private Integer getPageSize(Integer pageSize) {
        if (pageSize == null)
            return DEFAULT_PAGE_SIZE;

        if (pageSize > MAX_PAGE_SIZE)
            return MAX_PAGE_SIZE;

        return pageSize;
    }
}
