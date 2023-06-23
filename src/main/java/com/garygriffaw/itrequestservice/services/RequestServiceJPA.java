package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.mappers.RequestMapper;
import com.garygriffaw.itrequestservice.mappers.UserMapper;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.model.UserDTO;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class RequestServiceJPA implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int MAX_PAGE_SIZE = 1000;


    @Override
    public Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Request> requestPage;

        requestPage = requestRepository.findAll(pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Page<RequestDTO> listRequestsByRequester(String requesterUsername, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Request> requestPage;

        Optional<UserDTO> requesterDTO = userService.getUserByUserName(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Page.empty();
        }

        User requester = userMapper.userDTOToUser(requesterDTO.get());
        requestPage = requestRepository.findAllByRequester(requester, pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Optional<RequestDTO> getRequestById(Integer requestId) {
        return Optional.ofNullable(requestMapper.requestToRequestDTO(requestRepository.findById(requestId)
                .orElse(null)));
    }

    @Override
    public Optional<RequestDTO> getRequestByIdAndRequester(Integer requestId, String requesterUsername) {
        Optional<UserDTO> requesterDTO = userService.getUserByUserName(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Optional.empty();
        }

        User requester = userMapper.userDTOToUser(requesterDTO.get());

        return Optional.ofNullable(requestMapper.requestToRequestDTO(requestRepository.findByIdAndRequester(requestId, requester)
                .orElse(null)));
    }

    @Override
    public RequestDTO saveNewRequest(RequestDTO requestDTO, HttpServletRequest httpRequest) {
        requestDTO.setRequester(getCurrentUserDTO(httpRequest).get());

        return requestMapper.requestToRequestDTO(requestRepository.save(requestMapper.requestDTOToRequest(requestDTO)));
    }

    @Override
    public Optional<RequestDTO> updateRequestById(Integer requestId, RequestDTO requestDTO) {
        AtomicReference<Optional<RequestDTO>> atomicReference =new AtomicReference<>();

        requestRepository.findById(requestId).ifPresentOrElse(foundRequest -> {
            foundRequest.setTitle(requestDTO.getTitle());
            foundRequest.setDescription(requestDTO.getDescription());
            foundRequest.setResolution(requestDTO.getResolution());
            atomicReference.set(Optional.of(requestMapper.requestToRequestDTO(requestRepository.save(foundRequest))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }


    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = getPageNumber(pageNumber);
        int queryPageSize = getPageSize(pageSize);

        Sort sort = Sort.by(Sort.Order.asc("createdDate"));

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

    private Optional<UserDTO> getCurrentUserDTO(HttpServletRequest httpRequest) {
        final String authHeader = httpRequest.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        jwt = authHeader.substring(7);

        return Optional.ofNullable(userService.getUserByUserName(jwtService.extractUsername(jwt)))
                .orElse(null);
    }
}
