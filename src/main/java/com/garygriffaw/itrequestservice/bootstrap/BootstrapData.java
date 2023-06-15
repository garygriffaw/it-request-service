package com.garygriffaw.itrequestservice.bootstrap;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
import com.garygriffaw.itrequestservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final static String TEST_USER_1 = "test_user_1";
    private final static String TEST_USER_2 = "test_user_2";

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadUserData();
        loadRequestData();
    }

    private void loadUserData() {
        User user1 = User.builder()
                .username(TEST_USER_1)
                .firstname("Test 1")
                .lastname("Smith")
                .email("test1@mail.abc")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username(TEST_USER_2)
                .firstname("Test 2")
                .lastname("Jones")
                .email("test2@mail.abc")
                .build();
        userRepository.save(user2);
    }

    private void loadRequestData() {
        User testUser1 = userRepository.findByUsername(TEST_USER_1).get();
        User testUser2 = userRepository.findByUsername(TEST_USER_2).get();

        Request request1 = Request.builder()
                .title("Request 1")
                .description("This is the description for request 1.")
                .requester(testUser1)
                .build();
        requestRepository.save(request1);

        Request request2 = Request.builder()
                .title("Request 2")
                .description("This is the description for request 2.")
                .requester(testUser2)
                .build();
        requestRepository.save(request2);

        Request request3 = Request.builder()
                .title("Request 3")
                .description("This is the description for request 3.")
                .requester(testUser2)
                .build();
        requestRepository.save(request3);
    }
}
