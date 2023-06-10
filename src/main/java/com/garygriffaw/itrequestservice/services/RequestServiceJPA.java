package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.mappers.RequestMapper;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
@RequiredArgsConstructor
public class RequestServiceJPA implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int MAX_PAGE_SIZE = 1000;


    @Override
    public Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest= buildPageRequest(pageNumber, pageSize);

        Page<Request> requestPage;

        requestPage = requestRepository.findAll(pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Optional<RequestDTO> getRequestById(UUID requestId) {
        return Optional.ofNullable(requestMapper.requestToRequestDTO(requestRepository.findById(requestId)
                .orElse(null)));
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
}
