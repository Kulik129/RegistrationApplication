package ru.kulik.registration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User("John", "secret");
        userService.save(user);

        User savedUser = userService.getUser(user.getId());

        assertNotNull(savedUser);

        assertEquals("John", savedUser.getName());
        assertEquals("secret", savedUser.getPassword());
    }

    @Test
    void testGetUser() {
        User user = new User("Alice", "mypassword");
        userService.save(user);

        User savedUser = userService.getUser(user.getId());

        assertNotNull(savedUser);

        assertEquals("Alice", savedUser.getName());
        assertEquals("mypassword", savedUser.getPassword());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Bob", "123456");
        userService.save(user);

        User savedUser = userService.getUser(user.getId());

        assertNotNull(savedUser);

        userService.delete(savedUser.getId());

        User deletedUser = userService.getUser(savedUser.getId());

        assertNull(deletedUser);
    }
}
