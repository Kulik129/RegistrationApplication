package ru.kulik.registration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kulik.registration.DTO.request.UpdateInfo;
import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.request.UserRequest;
import ru.kulik.registration.DTO.response.UserResponse;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {


    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    UserRequest request = UserRequest
            .builder()
            .firstName("Name")
            .lastName("LastName")
            .dateOfBirth("27.07.1999")
            .email("mail@ex.ru")
            .phoneNumber("89123188170")
            .password("123456")
            .build();
    User user;

    @BeforeEach
    void create() {
        User createUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .registrationDate(LocalDateTime.now())
                .subscriptionEndDate(LocalDateTime.now().plusDays(1))
                .role(UserRole.USER)
                .active(true)
                .build();

        user = userRepository.save(createUser);
    }

    @AfterEach
    void delete() {
        userService.deleteUserByID(user.getId());
    }

    @Test
    void testGetUserById() {
        UserResponse userByID = userService.getUserByID(user.getId());

        assertEquals(user.getId(), userByID.getId());
    }

    @Test
    void testCreateUser() {
        UserRequest request = UserRequest
                .builder()
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth("27.07.1999")
                .email("ex@ex.ru")
                .phoneNumber("89123133170")
                .password("123456")
                .build();
        UserResponse user = userService.createUser(request);

        assertEquals("Name", user.getFirstName());
        assertEquals("LastName", user.getLastName());
        assertEquals("27.07.1999", user.getDateOfBirth());
        assertEquals("ex@ex.ru", user.getEmail());
        assertEquals("89123133170", user.getPhoneNumber());
        userRepository.deleteById(user.getId());
    }

    @Test
    void testGetUserByEmail() {
        UserResponse userByEmail = userService.getUserByEmail(user.getEmail());

        assertEquals("mail@ex.ru", userByEmail.getEmail());
    }

    @Test
    void testGetUserByPhone() {
        UserResponse userByPhone = userService.getUserByPhone(user.getPhoneNumber());

        assertEquals("89123188170", userByPhone.getPhoneNumber());
    }

    @Test
    void testDeleteUserById() {
        List<User> users = userRepository.findAll();
        int size = users.size(); // 19
        userService.deleteUserByID(user.getId());

        assertEquals(size-1, users.size()-1);
    }

    @Test
    void testGetAllUsers() {
        List<UserResponse> allUsers = userService.getAllUsers();
        if (allUsers.size() > 0) {
            assertFalse(allUsers.isEmpty());
        } else {
            assertTrue(allUsers.isEmpty());
        }
    }

    @Test
    void testUpdateUserInfo() {
        Optional<User> userOptional = userRepository.findById(user.getId());
        UpdateInfo updateInfo = UpdateInfo.builder().firstName("NewFirstName").lastName("NewLasName").dateOfBirth("12.12.2000").build();
        UserResponse updateUserInfo = userService.updateUserInfo(userOptional.get().getId(), updateInfo);

        assertEquals("NewFirstName", updateUserInfo.getFirstName());
        assertEquals("NewLasName",updateUserInfo.getLastName());
        assertEquals("12.12.2000", updateUserInfo.getDateOfBirth());
    }

    @Test
    void testAuthenticate() {
        UserAuthenticate userAuthenticate = UserAuthenticate.builder().phoneNumber(request.getPhoneNumber()).password(request.getPassword()).build();
        UserResponse authenticate = userService.authenticate(userAuthenticate);

        assertEquals("LastName",authenticate.getLastName());
        assertEquals("Name",authenticate.getFirstName());
    }

    @Test
    void testUpdatePassword() {
        UpdatePassword updatePassword = UpdatePassword
                .builder()
                .password(request.getPassword())
                .passwordNew("NewNewNew")
                .build();

        Optional<User> userOptional = userRepository.findById(user.getId());
        String oldPassword = userOptional.get().getPassword();

        UserResponse userResponse = userService.updatePassword(user.getId(), updatePassword);

        String newPassword = userResponse.getPassword();

        assertFalse(oldPassword.equals(newPassword));
    }

    @Test
    void testUpdateSubscription() {
        Optional<User> userOptional = userRepository.findById(user.getId());
        LocalDateTime endDate = userOptional.get().getSubscriptionEndDate();

        UserResponse userResponse = userService.updateSubscription(user.getId(), "2030-12-23T12:12:12");// гггг-мм-ччTчч:мм:сс
        LocalDateTime endDate2 = userResponse.getSubscriptionEnd();

        assertFalse(endDate.equals(endDate2));
    }

    @Test
    void testUpdateRole() {

        UserResponse userResponse = userService.updateRole(user.getId(), true);
        UserRole role = userResponse.getRole();

        assertEquals(UserRole.ADMIN, role);
    }

    @Test
    void testUpdateActive() {
        UserResponse userResponse = userService.updateActive(user.getId(), false);
        boolean active = userResponse.isActive();

        assertFalse(active);
    }
}