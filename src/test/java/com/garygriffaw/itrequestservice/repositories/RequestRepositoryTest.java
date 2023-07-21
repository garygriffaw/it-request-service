package com.garygriffaw.itrequestservice.repositories;

import com.garygriffaw.itrequestservice.bootstrap.BootstrapData;
import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.RequestStatus;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(BootstrapData.class)
class RequestRepositoryTest {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestStatusRepository requestStatusRepository;

    @Transactional
    @Test
    void testSaveRequest() {
        User testUser = userRepository.findByUsername(BootstrapData.TEST_USER_1).get();
        RequestStatus requestStatus = requestStatusRepository.findByRequestStatus(RequestStatusEnum.CREATED).get();

        Request testRequest = Request.builder()
                .title("Test Title")
                .description("Test Description")
                .requester(testUser)
                .requestStatus(requestStatus)
                .build();

        Request savedRequest = requestRepository.save(testRequest);

        requestRepository.flush();

        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getId()).isNotNull();
    }

    @Test
    void testSaveRequestTitleTooShort() {
        User testUser = userRepository.findByUsername(BootstrapData.TEST_USER_1).get();

        assertThrows(ConstraintViolationException.class, () -> {
            Request testRequest = Request.builder()
                    .title("Test")
                    .description("Test Description")
                    .requester(testUser)
                    .build();

            requestRepository.save(testRequest);

            requestRepository.flush();
        });
    }

    @Test
    void testSaveRequestTitleTooLong() {
        User testUser = userRepository.findByUsername(BootstrapData.TEST_USER_1).get();

        assertThrows(ConstraintViolationException.class, () -> {
            Request testRequest = Request.builder()
                    .title("123456789012345678901234567890123456789012345678901")
                    .description("Test Description")
                    .requester(testUser)
                    .build();

            requestRepository.save(testRequest);

            requestRepository.flush();
        });
    }

    @Test
    void testSaveRequestDescriptionTooShort() {
        User testUser = userRepository.findByUsername(BootstrapData.TEST_USER_1).get();

        assertThrows(ConstraintViolationException.class, () -> {
            Request testRequest = Request.builder()
                    .title("Test Title")
                    .description("Test")
                    .requester(testUser)
                    .build();

            requestRepository.save(testRequest);

            requestRepository.flush();
        });
    }
}