package com.garygriffaw.itrequestservice.bootstrap;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
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

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadRequestData();
    }

    private void loadRequestData() {
        Request request1 = Request.builder()
                .id(UUID.randomUUID())
                .version(1)
                .title("Request 1")
                .description("This is the description for request 1.")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestRepository.save(request1);

        Request request2 = Request.builder()
                .id(UUID.randomUUID())
                .version(1)
                .title("Request 2")
                .description("This is the description for request 2.")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestRepository.save(request2);

        Request request3 = Request.builder()
                .id(UUID.randomUUID())
                .version(1)
                .title("Request 3")
                .description("This is the description for request 3.")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        requestRepository.save(request3);
    }
}
