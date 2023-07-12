package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.model.RequestAssignedToDTO;
import com.garygriffaw.itrequestservice.model.RequestDTO;
import com.garygriffaw.itrequestservice.model.RequestRequesterDTO;
import com.garygriffaw.itrequestservice.model.UserUnsecureDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private Map<Integer, RequestDTO> requestMap;

    public RequestServiceImpl() {
        this.requestMap = new HashMap<>();

        UserUnsecureDTO user1 = UserUnsecureDTO.builder()
                .username("user1")
                .firstname("John")
                .lastname("Doe")
                .build();

        UserUnsecureDTO user2 = UserUnsecureDTO.builder()
                .username("user2")
                .firstname("Jane")
                .lastname("Smith")
                .build();

        UserUnsecureDTO tech1 = UserUnsecureDTO.builder()
                .username("tech1")
                .firstname("Sam")
                .lastname("Brown")
                .build();

        UserUnsecureDTO tech2 = UserUnsecureDTO.builder()
                .username("tech2")
                .firstname("Sussie")
                .lastname("Washington")
                .build();

        RequestDTO request1 = RequestDTO.builder()
                .id(1)
                .version(1)
                .title("Request 1")
                .description("Software install")
                .requester(user1)
                .assignedTo(tech1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request1.getId(), request1);

        RequestDTO request2 = RequestDTO.builder()
                .id(2)
                .version(1)
                .title("Request 2")
                .description("Reset password")
                .requester(user2)
                .assignedTo(tech2)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request2.getId(), request2);

        RequestDTO request3 = RequestDTO.builder()
                .id(3)
                .version(1)
                .title("Request 3")
                .description("Install software")
                .requester(user1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request3.getId(), request3);

        RequestDTO request4 = RequestDTO.builder()
                .id(4)
                .version(1)
                .title("Request 4")
                .description("New monitor")
                .requester(user2)
                .assignedTo(tech1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestMap.put(request4.getId(), request4);
    }

    @Override
    public Page<RequestDTO> listRequests(Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(requestMap.values()));
    }

    @Override
    public Page<RequestDTO> listRequestsByRequester(String requesterUsername, Integer pageNumber, Integer pageSize) {
        Map<Integer, RequestDTO> requestByRequesterMap = requestMap.entrySet()
                .stream()
                .filter(map -> "user1".equals(map.getValue().getRequester().getUsername()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new PageImpl<>(new ArrayList<>(requestByRequesterMap.values()));
    }

    @Override
    public Page<RequestDTO> listRequestsByAssignedTo(String assignedToUsername, Integer pageNumber, Integer pageSize) {
        Map<Integer, RequestDTO> requestByAssignedToMap = requestMap.entrySet()
                .stream()
                .filter(map -> map.getValue().getAssignedTo() != null)
                .filter(map -> "tech1".equals(map.getValue().getAssignedTo().getUsername()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new PageImpl<>(new ArrayList<>(requestByAssignedToMap.values()));
    }

    @Override
    public Page<RequestDTO> listRequestsByDescriptionContainingIgnoreCase(String description, Integer pageNumber, Integer pageSize) {
        Map<Integer, RequestDTO> filteredMap = requestMap.entrySet()
                .stream()
                .filter(map -> map.getValue().getDescription().contains(description))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new PageImpl<>(new ArrayList<>(filteredMap.values()));
    }

    @Override
    public Optional<RequestDTO> getRequestById(Integer requestId) {
        return Optional.of(requestMap.get(requestId));
    }

    @Override
    public Optional<RequestDTO> getRequestByIdAndRequester(Integer requestId, String requesterUsername) {
        return Optional.of(requestMap.get(requestId));
    }

    @Override
    public Optional<RequestDTO> getRequestByIdAndAssignedTo(Integer requestId, String assignedToUsername) {
        return Optional.of(requestMap.get(requestId));
    }

    @Override
    public Optional<RequestDTO> saveNewRequest(RequestRequesterDTO requestDTO, String requesterUsername) {
        RequestDTO savedRequest = RequestDTO.builder()
                .id(4)
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .build();

        requestMap.put(savedRequest.getId(), savedRequest);

        return Optional.of(savedRequest);
    }

    @Override
    public Optional<RequestDTO> updateRequestById(Integer requestId, RequestDTO requestDTO) {
        RequestDTO existing = requestMap.get(requestId);
        existing.setTitle(requestDTO.getTitle());
        existing.setDescription(requestDTO.getDescription());
        existing.setResolution(requestDTO.getResolution());

        return Optional.of(existing);
    }

    @Override
    public Optional<RequestDTO> updateRequestByIdAndRequester(Integer requestId, String requesterUsername, RequestRequesterDTO requestDTO) {
        RequestDTO existing = requestMap.get(requestId);
        existing.setTitle(requestDTO.getTitle());
        existing.setDescription(requestDTO.getDescription());

        return Optional.of(existing);
    }

    @Override
    public Optional<RequestDTO> updateRequestByIdAndAssignedTo(Integer requestId, String assignedToUsername, RequestAssignedToDTO requestDTO) {
        RequestDTO existing = requestMap.get(requestId);
        existing.setResolution(requestDTO.getResolution());

        return Optional.of(existing);
    }

    @Override
    public boolean deleteById(Integer requestId) {
        requestMap.remove(requestId);

        return true;
    }
}
