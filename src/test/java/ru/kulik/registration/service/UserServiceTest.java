package ru.kulik.registration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.implement.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testSaveUser() {
        User user = new User("John", "Smith", "23.12.1968", "smith.jo@gmail.com", "89255555555", "secret");
        userService.save(user);

        Optional<User> savedUser = userService.getUserByID(user.getId());

        assertNotNull(savedUser);

        assertEquals("John", savedUser.get().getFirstName());
        assertEquals("Smith", savedUser.get().getLastName());
        assertEquals("23.12.1968", savedUser.get().getDateOfBirth());
        assertEquals("smith.jo@gmail.com", savedUser.get().getEmail());
        assertEquals("89255555555", savedUser.get().getPhoneNumber());
        assertTrue(passwordEncoder.matches("secret", user.getPassword()), savedUser.get().getPassword());
    }

    @Test
    void testGetUser() {
        User user = new User("Alice", "Festa", "12.12.2000", "fest@gmail.com", "89858888888", "mypassword");
        userService.save(user);

        Optional<User> savedUser = userService.getUserByID(user.getId());

        assertNotNull(savedUser);

        assertEquals("Alice", savedUser.get().getFirstName());
        assertEquals("Festa", savedUser.get().getLastName());
        assertEquals("12.12.2000", savedUser.get().getDateOfBirth());
        assertEquals("fest@gmail.com", savedUser.get().getEmail());
        assertEquals("89858888888", savedUser.get().getPhoneNumber());
        assertTrue(passwordEncoder.matches("mypassword", user.getPassword()), savedUser.get().getPassword());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Bob", "Marlie", "18.11.1967", "marlie@gmai.com", "89681111111", "123456");
        userService.save(user);

        Optional<User> savedUser = userService.getUserByID(user.getId());

        assertNotNull(savedUser);

        userService.delete(savedUser.get().getId());

        Optional<User> deletedUser = userService.getUserByID(savedUser.get().getId());

        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();

        assertNotNull(users);

        System.out.println("Количество пользователей в БД: " + users.size());

        assertEquals(users.size(), userService.getAllUsers().size());
        assertFalse(users.isEmpty());
    }

    @Test
    void testGetByEmail() {
        User user = new User("Polli", "Shelby", "28.10.1967", "poll@gmai.com", "89681111111", "123456");
        userService.save(user);

        Optional<User> savedUser = userService.getUserByEmail(user.getEmail());

        assertEquals("poll@gmai.com", savedUser.get().getEmail());
    }

    @Test
    void testGetUserByPhone() {
        User user = new User("Grey", "Shelby", "18.10.1967", "grey@gmai.com", "89685678945", "123456");

        userService.save(user);

        Optional<User> savedUser = userService.getUserByPhone(user.getPhoneNumber());

        assertEquals("89685678945", savedUser.get().getPhoneNumber());
    }

    @Test
    void testExistsByEmail() {
        User user = new User("Ada", "Shelby", "12.03.1977", "ada@gmai.com", "89689874534", "123456");

        userService.save(user);
        boolean savedUser = userServiceImpl.existsByEmail(user.getEmail());

        assertTrue(savedUser);
    }

    @Test
    void testExistsByPhoneNumber() {
        User user = new User("Ada", "Shelby", "12.03.1977", "ada@gmai.com", "89689874534", "123456");

        userService.save(user);
        boolean savedUser = userServiceImpl.existsByPhoneNumber(user.getPhoneNumber());

        assertTrue(savedUser);
    }
}
