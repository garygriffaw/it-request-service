package com.garygriffaw.itrequestservice.bootstrap;

import com.garygriffaw.itrequestservice.entities.Request;
import com.garygriffaw.itrequestservice.entities.Role;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.enums.RoleEnum;
import com.garygriffaw.itrequestservice.repositories.RequestRepository;
import com.garygriffaw.itrequestservice.repositories.RoleRepository;
import com.garygriffaw.itrequestservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public final static String TEST_USER_1 = "test_user_1";
    public final static String TEST_USER_2 = "test_user_2";
    public final static String TEST_TECH_1 = "test_tech_1";
    public final static String TEST_TECH_2 = "test_tech_2";

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadRoleData();
        loadUserData();
        loadRequestData();
    }

    private void loadRoleData() {
        Role userRole = Role.builder()
                .role(RoleEnum.USER)
                .build();
        roleRepository.save(userRole);

        Role adminRole = Role.builder()
                .role(RoleEnum.ADMIN)
                .build();
        roleRepository.save(adminRole);
    }

    private void loadUserData() {
        Role userRole = roleRepository.findByRole(RoleEnum.USER).get();

        User user1 = User.builder()
                .username(TEST_USER_1)
                .firstname("Test 1")
                .lastname("Smith")
                .email("test1@mail.abc")
                .build();
        user1.addRole(userRole);
        userRepository.save(user1);

        User user2 = User.builder()
                .username(TEST_USER_2)
                .firstname("Test 2")
                .lastname("Jones")
                .email("test2@mail.abc")
                .build();
        user2.addRole(userRole);
        userRepository.save(user2);

        User tech1 = User.builder()
                .username(TEST_TECH_1)
                .firstname("Tech 1")
                .lastname("Johnson")
                .email("tech1@mail.abc")
                .build();
        user1.addRole(userRole);
        userRepository.save(tech1);

        User tech2 = User.builder()
                .username(TEST_TECH_2)
                .firstname("Tech 2")
                .lastname("Gavin")
                .email("tech2@mail.abc")
                .build();
        user1.addRole(userRole);
        userRepository.save(tech2);
    }

    private void loadRequestData() {
        User testUser1 = userRepository.findByUsername(TEST_USER_1).get();
        User testUser2 = userRepository.findByUsername(TEST_USER_2).get();
        User testTech1 = userRepository.findByUsername(TEST_TECH_1).get();
        User testTech2 = userRepository.findByUsername(TEST_TECH_2).get();

        Request request1 = Request.builder()
                .title("Request 1")
                .description("This is the description for request 1.")
                .requester(testUser1)
                .assignedTo(testTech2)
                .build();
        requestRepository.save(request1);

        Request request2 = Request.builder()
                .title("Request 2")
                .description("This is the description for request 2.")
                .requester(testUser2)
                .assignedTo(testTech1)
                .build();
        requestRepository.save(request2);

        Request request3 = Request.builder()
                .title("Request 3")
                .description("This is the description for request 3.")
                .requester(testUser2)
                .build();
        requestRepository.save(request3);

        Request request4 = Request.builder()
                .title("Request 4")
                .description("This is the description for request 4.")
                .requester(testUser1)
                .assignedTo(testTech1)
                .build();
        requestRepository.save(request4);
    }
}
