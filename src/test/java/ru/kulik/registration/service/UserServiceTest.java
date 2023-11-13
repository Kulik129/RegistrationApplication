package ru.kulik.registration.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.model.User;
import ru.kulik.registration.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = new User("John", "Smith", "23.12.1968", "smith.jo@gmail.com","79255555555", "secret");
        userService.save(user);

        Optional<User> savedUser = userService.getUserByID(user.getId());

        assertNotNull(savedUser);

        assertEquals("John", savedUser.get().getName());
        assertEquals("Smith", savedUser.get().getSurname());
        assertEquals("23.12.1968", savedUser.get().getDateOfBirth());
        assertEquals("smith.jo@gmail.com", savedUser.get().getEmail());
        assertEquals("79255555555", savedUser.get().getPhoneNumber());
        assertEquals("secret", savedUser.get().getPassword());
    }

    @Test
    void testGetUser() {
        User user = new User("Alice", "Festa", "12.12.2000", "fest@gmail.com","79858888888", "mypassword");
        userService.save(user);

        Optional<User> savedUser = userService.getUserByID(user.getId());

        assertNotNull(savedUser);

        assertEquals("Alice", savedUser.get().getName());
        assertEquals("Festa", savedUser.get().getSurname());
        assertEquals("12.12.2000", savedUser.get().getDateOfBirth());
        assertEquals("fest@gmail.com", savedUser.get().getEmail());
        assertEquals("79858888888", savedUser.get().getPhoneNumber());
        assertEquals("mypassword", savedUser.get().getPassword());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Bob", "Marlie", "18.11.1967", "marlie@gmai.com","9681111111", "123456");
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
}
