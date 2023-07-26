package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.RequestStatus;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import com.garygriffaw.itrequestservice.exceptions.InvalidCombinationException;
import com.garygriffaw.itrequestservice.exceptions.ValueNotFoundException;
import com.garygriffaw.itrequestservice.mappers.RequestMapper;
import com.garygriffaw.itrequestservice.mappers.RequestStatusMapper;
import com.garygriffaw.itrequestservice.mappers.UserMapper;
import com.garygriffaw.itrequestservice.model.*;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RequestStatusService requestStatusService;
    private final RequestStatusMapper requestStatusMapper;
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

        Optional<UserUnsecureDTO> requesterDTO = userService.getUserByUsernameUnsec(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Page.empty();
        }

        User requester = userMapper.userUnsecureDTOToUser(requesterDTO.get());
        requestPage = requestRepository.findByRequester(requester, pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Page<RequestDTO> listRequestsByRequesterAndDescription(String requesterUsername, String description,
                                                                  Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Request> requestPage;

        Optional<UserUnsecureDTO> requesterDTO = userService.getUserByUsernameUnsec(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Page.empty();
        }

        User requester = userMapper.userUnsecureDTOToUser(requesterDTO.get());
        String descLike = getLikeValue(description);
        requestPage = requestRepository.findByRequesterAndDescription(requester, descLike, pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Page<RequestDTO> listRequestsByAssignedTo(String assignedToUsername, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Request> requestPage;

        Optional<UserUnsecureDTO> assignedToDTO = userService.getUserByUsernameUnsec(assignedToUsername);

        if (assignedToDTO.isEmpty()) {
            return Page.empty();
        }

        User assignedTo = userMapper.userUnsecureDTOToUser(assignedToDTO.get());
        requestPage = requestRepository.findByAssignedTo(assignedTo, pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Page<RequestDTO> listRequestsByDescriptionContainingIgnoreCase(String description, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Request> requestPage;

        requestPage = requestRepository.findByDescriptionContainingIgnoreCase(description, pageRequest);

        return requestPage.map(requestMapper::requestToRequestDTO);
    }

    @Override
    public Optional<RequestDTO> getRequestById(Integer requestId) {
        return Optional.ofNullable(requestMapper.requestToRequestDTO(requestRepository.findById(requestId)
                .orElse(null)));
    }

    @Override
    public Optional<RequestDTO> getRequestByIdAndRequester(Integer requestId, String requesterUsername) {
        Optional<UserUnsecureDTO> requesterDTO = userService.getUserByUsernameUnsec(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Optional.empty();
        }

        User requester = userMapper.userUnsecureDTOToUser(requesterDTO.get());

        return Optional.ofNullable(requestMapper.requestToRequestDTO(requestRepository.findByIdAndRequester(requestId, requester)
                .orElse(null)));
    }

    @Override
    public Optional<RequestDTO> getRequestByIdAndAssignedTo(Integer requestId, String assignedToUsername) {
        Optional<UserUnsecureDTO> assignedToDTO = userService.getUserByUsernameUnsec(assignedToUsername);

        if (assignedToDTO.isEmpty()) {
            return Optional.empty();
        }

        User assignedTo = userMapper.userUnsecureDTOToUser(assignedToDTO.get());

        return Optional.ofNullable(requestMapper.requestToRequestDTO(requestRepository.findByIdAndAssignedTo(requestId, assignedTo)
                .orElse(null)));
    }

    @Override
    public Optional<RequestDTO> saveNewRequest(RequestCreateDTO requestRequesterDTO, String requesterUsername) {
        Optional<UserUnsecureDTO> requesterDTO = userService.getUserByUsernameUnsec(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Optional.empty();
        }

        Optional<RequestStatusDTO> requestStatusDTO = requestStatusService.getRequestStatusByRequestStatus(RequestStatusEnum.CREATED);

        if (requestStatusDTO.isEmpty()) {
            return Optional.empty();
        }

        RequestDTO requestDTO = RequestDTO.builder()
                .title(requestRequesterDTO.getTitle())
                .description(requestRequesterDTO.getDescription())
                .requester(requesterDTO.get())
                .requestStatus(requestStatusDTO.get())
                .build();

        return Optional.of(requestMapper.requestToRequestDTO(requestRepository.save(requestMapper.requestDTOToRequest(requestDTO))));
    }

    @Override
    public Optional<RequestDTO> updateRequestById(Integer requestId, RequestDTO requestDTO) {
        AtomicReference<Optional<RequestDTO>> atomicReference =new AtomicReference<>();

        User requester = getRequesterUser(requestDTO.getRequester());
        User assignedTo = getAssignedToUser(requestDTO.getAssignedTo());
        RequestStatus requestStatus = getRequestStatus(requestDTO.getRequestStatus());

        requestRepository.findById(requestId).ifPresentOrElse(foundRequest -> {
            foundRequest.setTitle(requestDTO.getTitle());
            foundRequest.setDescription(requestDTO.getDescription());
            foundRequest.setRequester(requester);
            foundRequest.setRequestStatus(requestStatus);
            foundRequest.setAssignedTo(assignedTo);
            foundRequest.setResolution(requestDTO.getResolution());
            atomicReference.set(Optional.of(requestMapper.requestToRequestDTO(requestRepository.save(foundRequest))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Optional<RequestDTO> updateRequestByIdAndRequester(Integer requestId, String requesterUsername, RequestRequesterDTO requestDTO) {
        AtomicReference<Optional<RequestDTO>> atomicReference = new AtomicReference<>();

        Optional<UserUnsecureDTO> requesterDTO = userService.getUserByUsernameUnsec(requesterUsername);

        if (requesterDTO.isEmpty()) {
            return Optional.empty();
        }

        User requester = userMapper.userUnsecureDTOToUser(requesterDTO.get());

        requestRepository.findByIdAndRequester(requestId, requester).ifPresentOrElse(foundRequest -> {
            Request validatedRequest = getValidatedRequestUpdateByRequester(requestDTO, foundRequest);
            atomicReference.set(Optional.of(requestMapper.requestToRequestDTO(requestRepository.save(validatedRequest))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Optional<RequestDTO> updateRequestByIdAndAssignedTo(Integer requestId, String assignedToUsername, RequestAssignedToDTO requestDTO) {
        AtomicReference<Optional<RequestDTO>> atomicReference = new AtomicReference<>();

        Optional<UserUnsecureDTO> assignedToDTO = userService.getUserByUsernameUnsec(assignedToUsername);

        if (assignedToDTO.isEmpty()) {
            return Optional.empty();
        }

        User assignedTo = userMapper.userUnsecureDTOToUser(assignedToDTO.get());

        requestRepository.findByIdAndAssignedTo(requestId, assignedTo).ifPresentOrElse(foundRequest -> {
            Request validatedRequest = getValidatedRequestUpdateByAssignedTo(requestDTO, foundRequest);
            atomicReference.set(Optional.of(requestMapper.requestToRequestDTO(requestRepository.save(validatedRequest))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public boolean deleteById(Integer requestId) {
        if (requestRepository.existsById(requestId)) {
            requestRepository.deleteById(requestId);
            return true;
        }

        return false;
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

    private Optional<UserUnsecureDTO> getCurrentUserDTO(HttpServletRequest httpRequest) {
        final String authHeader = httpRequest.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        jwt = authHeader.substring(7);

        return Optional.ofNullable(userService.getUserByUsernameUnsec(jwtService.extractUsername(jwt)))
                .orElse(null);
    }

    private Optional<UserUnsecureDTO> getUserDTO(String username) {
        return Optional.ofNullable(userService.getUserByUsernameUnsec(username))
                .orElse(null);
    }

    private User getRequesterUser(UserUnsecureDTO requester) {
        try {
            return getUser(requester, true);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new ValueNotFoundException("Requester not found");
        }
    }

    private User getAssignedToUser(UserUnsecureDTO assignedTo) {
        try {
            return getUser(assignedTo, false);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new ValueNotFoundException("Assigned To not found");
        }
    }

    private User getUser(UserUnsecureDTO userUnsecureDTO, boolean required) throws ChangeSetPersister.NotFoundException {
        if (userUnsecureDTO == null) {
            if (required) {
                throw new ChangeSetPersister.NotFoundException();
            }
            return null;
        }

        Optional<UserUnsecureDTO> foundUserUnsecureDTO = userService.getUserByUsernameUnsec(userUnsecureDTO.getUsername());

        return foundUserUnsecureDTO.map(userMapper::userUnsecureDTOToUser)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    private RequestStatus getRequestStatus(RequestStatusDTO requestStatusDTO) {
        try {
            if (requestStatusDTO == null) {
                throw new ChangeSetPersister.NotFoundException();
            }

            Optional<RequestStatusEnum> requestStatusEnum = RequestStatusEnum.findByName(requestStatusDTO.getRequestStatusCode());

            if (requestStatusEnum.isEmpty()) {
                throw new ChangeSetPersister.NotFoundException();
            }

            Optional<RequestStatusDTO> foundRequestStatusDTO =
                    requestStatusService.getRequestStatusByRequestStatus(requestStatusEnum.get());

            return foundRequestStatusDTO.map(requestStatusMapper::requestStatusDTOToRequestStatus)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new ValueNotFoundException("Request Status not found");
        }
    }

    private String getLikeValue(String value) {
        return "%" + value.trim() + "%";
    }

    private Request getValidatedRequestUpdateByRequester(RequestRequesterDTO requestDTO, Request currentRequest) {
        RequestStatus newRequestStatus = getRequestStatus(requestDTO.getRequestStatus());

        if (!(newRequestStatus.getRequestStatusCode() == currentRequest.getRequestStatus().getRequestStatusCode() ||
            newRequestStatus.getRequestStatusCode().isValidRequestStatusForRequester)) {
          throw new InvalidCombinationException("Can not change the status to " + newRequestStatus.getRequestStatusDisplay());
        }

        validateCompletedRequestStatusValues(newRequestStatus, requestDTO.getResolution());

        currentRequest.setTitle(requestDTO.getTitle());
        currentRequest.setDescription(requestDTO.getDescription());
        currentRequest.setRequestStatus(newRequestStatus);
        currentRequest.setResolution(requestDTO.getResolution());
        return currentRequest;
    }

    private Request getValidatedRequestUpdateByAssignedTo(RequestAssignedToDTO requestDTO, Request currentRequest) {
        RequestStatus newRequestStatus = getRequestStatus(requestDTO.getRequestStatus());

        if (!(newRequestStatus.getRequestStatusCode() == currentRequest.getRequestStatus().getRequestStatusCode() ||
                newRequestStatus.getRequestStatusCode().isValidRequestStatusForAssignedTo)) {
            throw new InvalidCombinationException("Can not change the status to " + newRequestStatus.getRequestStatusDisplay());
        }

        validateCompletedRequestStatusValues(newRequestStatus, requestDTO.getResolution());

        currentRequest.setRequestStatus(newRequestStatus);
        currentRequest.setResolution(requestDTO.getResolution());
        return currentRequest;
    }

    private void validateCompletedRequestStatusValues(RequestStatus newRequestStatus, String resolution) {
        if ((newRequestStatus.getRequestStatusCode() == RequestStatusEnum.COMPLETE ||
                newRequestStatus.getRequestStatusCode() == RequestStatusEnum.CANCELLED) &&
                (resolution == null ||
                        resolution.trim().length() < 5)) {
            throw new InvalidCombinationException("Resolution must have at least 5 characters when status is " + newRequestStatus.getRequestStatusDisplay());
        }

        if (!(newRequestStatus.getRequestStatusCode() == RequestStatusEnum.COMPLETE ||
                newRequestStatus.getRequestStatusCode() == RequestStatusEnum.CANCELLED) &&
                resolution != null &&
                !resolution.trim().equals("")) {
            throw new InvalidCombinationException("Resolution must be empty when status is " + newRequestStatus.getRequestStatusDisplay());
        }
    }
}
