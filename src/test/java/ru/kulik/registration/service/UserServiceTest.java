package ru.kulik.registration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.DTO.UserDto;
import ru.kulik.registration.exception.UserNotFoundException;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;

    User user = new User(1,"Tom", "Shelby", "tom@example.com", "123456","89251112233");

    @Test
    void createUser() {
        UserDto userTest = new UserDto();
        userTest.setFirstName("Tom");
        userTest.setLastName("Shelby");
        userTest.setEmail("tom@example.com");
        userTest.setPassword("123456");


        when(passwordEncoder.encode(userTest.getPassword())).thenReturn("$2a$10$IxvI0pKiG.vSWkSY1CpcCePC901i8/hK68wm4KMEByz2EV1/HFIC.");

        userService.createUser(userTest);

        assertEquals("$2a$10$IxvI0pKiG.vSWkSY1CpcCePC901i8/hK68wm4KMEByz2EV1/HFIC.", userTest.getPassword());
        assertEquals(UserRole.USER, userTest.getRole());
        assertEquals(true, userTest.isActive());
        assertTrue(!"12345678".equals(userTest.getPassword()));
    }

    @Test
    void getUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber()));

        Optional<UserDto> result = userService.getUserByID(user.getId());

        assertTrue(result.isPresent());
        assertEquals("Tom", result.get().getFirstName());
        assertEquals("Shelby", result.get().getLastName());
        assertEquals("tom@example.com", result.get().getEmail());
        assertEquals("123456", result.get().getPassword());
    }

    @Test
    public void existsByEmail() {
        // Подготовка тестовых данных
        String existingEmail = "existing@example.com";

        // Настройка поведения mock объекта UserRepository
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // Вызов метода, который мы хотим протестировать
        boolean result = userService.existsByEmail(existingEmail);

        // Проверка, что метод возвращает true, так как пользователь существует
        assertTrue(result);
    }

    @Test
    void existsByEmailFalse() {
        // Подготавливаем тестовые данные
        String existingEmail = "tomas@shelby.com";

        // Настраиваем поведение mock объекта UserRepository для сценария существования пользователя
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // Вызываем метод, который мы хотим протестировать
        boolean result = userService.existsByEmail(existingEmail);

        // Проверяем, что метод возвращает true, так как пользователь существует
        assertTrue(result);
    }
    @Test
    void existsByPhoneNumber() {
        String number = "89251112233";
        when(userRepository.existsByPhoneNumber(number)).thenReturn(true);

        boolean result = userService.existsByPhoneNumber(number);

        assertTrue(result);
    }

    @Test
    void existsByPhoneNumberFalse() {
        String number = "89251112233";
        when(userRepository.existsByPhoneNumber(number)).thenReturn(false);

        boolean result = userService.existsByPhoneNumber(number);

        assertFalse(result);
    }

    @Test
    void getUserByIdNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByID(userId));
    }

    @Test
    void getByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber()));

        Optional<UserDto> result = userService.getUserByEmail(user.getEmail());

        assertTrue(result.isPresent());
        assertEquals("Tom", result.get().getFirstName());
        assertEquals("Shelby", result.get().getLastName());
        assertEquals("tom@example.com", result.get().getEmail());
        assertEquals("123456", result.get().getPassword());
    }

    @Test
    void getUserByPhone() {
        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber()));

        Optional<UserDto> result = userService.getUserByPhone(user.getPhoneNumber());

        assertTrue(result.isPresent());
        assertEquals("Tom", result.get().getFirstName());
        assertEquals("Shelby", result.get().getLastName());
        assertEquals("tom@example.com", result.get().getEmail());
        assertEquals("89251112233",result.get().getPhoneNumber());
    }

    @Test
    void delete() {
        userService.delete(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1, "Tom", "Shelby", "tom@shelby.com", "654321","89251112233");
        User user2 = new User(2, "Artur", "Shelby", "artur@shelby.com", "123456","89252221133");

        List<User> mockUsers = Arrays.asList(user1, user2);

        // Ожидаем, что метод findAll в userRepository вернет mockUsers
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Вызываем метод, который мы хотим протестировать
        List<UserDto> result = userService.getAllUsers();

        // Проверяем, что результат соответствует ожидаемому списку UserDto
        assertEquals(2, result.size());
    }
}
