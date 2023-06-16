package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.RequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {

    private Map<UUID, RequestDTO> requestMap;

    public RequestServiceImpl() {
        this.requestMap = new HashMap<>();

        RequestDTO request1 = RequestDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .title("Request 1")
                .description("This is the description.")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request1.getId(), request1);

        RequestDTO request2 = RequestDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .title("Request 2")
                .description("This is the description.")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request2.getId(), request2);

        RequestDTO request3 = RequestDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .title("Request 3")
                .description("This is the description.")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request3.getId(), request3);
    }

    @Override
    public Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(requestMap.values()));
    }

    @Override
    public Optional<RequestDTO> getRequestById(UUID requestId) {
        return Optional.of(requestMap.get(requestId));
    }

    @Override
    public RequestDTO saveNewRequest(RequestDTO requestDTO, HttpServletRequest httpRequest) {
        RequestDTO savedRequest = RequestDTO.builder()
                .id(UUID.randomUUID())
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .build();

        requestMap.put(savedRequest.getId(), savedRequest);

        return savedRequest;
    }

    @Override
    public Optional<RequestDTO> updateRequestById(UUID requestId, RequestDTO requestDTO) {
        RequestDTO existing = requestMap.get(requestId);
        existing.setTitle(requestDTO.getTitle());
        existing.setDescription(requestDTO.getDescription());
        existing.setResolution(requestDTO.getResolution());

        return Optional.of(existing);
    }
}
