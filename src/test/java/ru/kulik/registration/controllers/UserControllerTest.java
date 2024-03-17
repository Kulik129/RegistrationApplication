package ru.kulik.registration.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.response.UserResponse;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    List<User> expected;

    @BeforeEach
    public void data() {
        expected = repository.saveAll(
                List.of(User.builder()
                                .firstName("Tom")
                                .lastName("Shelby")
                                .dateOfBirth("12.12.1923")
                                .email("shelbyT@company.com")
                                .phoneNumber("89233211234")
                                .password(passwordEncoder.encode("123456789"))
                                .registrationDate(LocalDateTime.now())
                                .subscriptionEndDate(LocalDateTime.now().plusDays(1))
                                .active(true)
                                .role(UserRole.USER)
                                .build(),
                        User.builder()
                                .firstName("Artur")
                                .lastName("Shelby")
                                .dateOfBirth("10.12.1919")
                                .email("shelbyA@dd.ru")
                                .phoneNumber("89238811234")
                                .password(passwordEncoder.encode("123456789"))
                                .registrationDate(LocalDateTime.now())
                                .subscriptionEndDate(LocalDateTime.now().plusDays(1))
                                .active(true)
                                .role(UserRole.USER)
                                .build()
                ));
    }
    @AfterEach
    public void deleteData() {
        repository.deleteAll();
    }
    @Test
    void testGetAll() {

        List<UserResponse> responseBody = webTestClient.get()
                .uri("/api/v1/users/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<UserResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(expected.size(), responseBody.size());

        for (UserResponse userResponse : responseBody) {
            boolean match = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), userResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getFirstName(), userResponse.getFirstName()));
            Assertions.assertTrue(match);

        }
    }

    @Test
    void testFindById() {

        String responseBody = webTestClient.get()
                .uri("/api/v1/users/" + expected.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
    }

    @Test
    void testNotFound() {
        Optional<Long> maxId = repository.findAll().stream()
                .map(User::getId)
                .max(Comparator.naturalOrder());

        Long nonExisting = maxId.orElse(0L);
        nonExisting++;

        webTestClient.get()
                .uri("/api/v1/users/" + nonExisting)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetUserByEmail() {
        String responseBody = webTestClient.get()
                .uri("/api/v1/users/by-email?email=" + expected.get(0).getEmail())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
    }

    @Test
    void testNotFoundByEmail() {
        webTestClient.get()
                .uri("/api/v1/users/by-email?email=" + "non@email.com")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetUserByPhone() {
        String responseBody = webTestClient.get()
                .uri("/api/v1/users/by-phone-number?phone=" + expected.get(0).getPhoneNumber())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
    }

    @Test
    void notFoundByPhoneNumber() {
        webTestClient.get()
                .uri("/api/v1/users/by-phone-number?phone=" + "89000000000")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteById() {
        List<UserResponse> responseBody = webTestClient.delete()
                .uri("/api/v1/users/delete/" + expected.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<UserResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(responseBody.size(), expected.size() - 1);
    }
    @Test
    void testAuthenticate() {
        UserAuthenticate userAuthenticate = UserAuthenticate.builder()
                .phoneNumber("89233211234")
                .password("123456789")
                .build();

        webTestClient.post()
                .uri("/api/v1/users/authenticate")
                .bodyValue(userAuthenticate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                });
    }

    @Test
    void testUpdatePassword() {
        UpdatePassword updatePassword = UpdatePassword.builder().password("123456789").passwordNew("987654321").build();
        webTestClient.put()
                .uri("/api/v1/users/password/" + expected.get(0).getId())
                .bodyValue(updatePassword)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                });
    }

    @Test
    void testUpdateSubscription() {
        String responseBody = webTestClient.put()
                .uri("/api/v1/users/subscription/" + expected.get(0).getId() + "?dateTime=" + "2030-12-12T12:12:12")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
    }

    @Test
    void testRole() {
        String responseBody = webTestClient.put()
                .uri("/api/v1/users/role/" + expected.get(0).getId() + "?param=" + "true")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
    }

    @Test
    void testActive() {
        String responseBody = webTestClient.put()
                .uri("/api/v1/users/active/" + expected.get(0).getId() + "?param" + "false")
                .exchange()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
    }
}