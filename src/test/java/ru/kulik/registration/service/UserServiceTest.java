package ru.kulik.registration.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.model.User;
import ru.kulik.registration.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = new User("John", "Smith", "23.12.1968", "smith.jo@gmail.com", "secret");
        userService.save(user);

        User savedUser = userService.getUser(user.getId());

        assertNotNull(savedUser);

        assertEquals("John", savedUser.getName());
        assertEquals("Smith", savedUser.getSurname());
        assertEquals("23.12.1968", savedUser.getDateOfBirth());
        assertEquals("smith.jo@gmail.com", savedUser.getEmail());
        assertEquals("secret", savedUser.getPassword());
    }

    @Test
    void testGetUser() {
        User user = new User("Alice", "Festa", "12.12.2000", "fest@gmail.com", "mypassword");
        userService.save(user);

        User savedUser = userService.getUser(user.getId());

        assertNotNull(savedUser);

        assertEquals("Alice", savedUser.getName());
        assertEquals("Festa", savedUser.getSurname());
        assertEquals("12.12.2000", savedUser.getDateOfBirth());
        assertEquals("fest@gmail.com", savedUser.getEmail());
        assertEquals("mypassword", savedUser.getPassword());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Bob", "Marlie", "18.11.1967", "marlie@gmai.com", "123456");
        userService.save(user);

        User savedUser = userService.getUser(user.getId());

        assertNotNull(savedUser);

        userService.delete(savedUser.getId());

        User deletedUser = userService.getUser(savedUser.getId());

        assertNull(deletedUser);
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
