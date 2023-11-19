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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.DTO.UserDto;
import ru.kulik.registration.exception.UserNotFoundException;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;

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

    @Test
    void testSaveUserWithValidPassword() {
        UserDto userDto = new UserDto("John", "Doe", "john@example.com", "123456");
        User user = new User("John", "Doe", "john@example.com", "123456");

        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.save(userDto));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        long userId = 1L;
        User user = new User("John", "Doe", "john@example.com", "123456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto("John", "Doe", "john@example.com", "123456"));

        Optional<UserDto> result = userService.getUserByID(userId);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        assertEquals("john@example.com", result.get().getEmail());
        assertEquals("123456", result.get().getPassword());
    }

    @Test
    void testGetUserByIdNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByID(userId));
    }

    @Test
    void testGetByEmail() {
        String email = "poll@gmai.com";
        User user = new User("Polli", "Shelby", "28.10.1967", "poll@gmai.com", "89681111111", "123456");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto("Polli", "Shelby", "28.10.1967", "poll@gmai.com", "89681111111", "123456"));

        Optional<UserDto> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals("Polli", result.get().getFirstName());
        assertEquals("Shelby", result.get().getLastName());
        assertEquals("poll@gmai.com", result.get().getEmail());
        assertEquals("123456", result.get().getPassword());
    }

    @Test
    void testGetUserByPhone() {
        String phone = "89685678945";
        User user = new User("Grey", "Shelby", "18.10.1967", "grey@gmai.com", "89685678945", "123456");

        when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto("Grey", "Shelby", "18.10.1967", "grey@gmai.com", "89685678945", "123456"));

        Optional<UserDto> result = userService.getUserByPhone(phone);

        assertTrue(result.isPresent());
        assertEquals("Grey", result.get().getFirstName());
        assertEquals("Shelby", result.get().getLastName());
        assertEquals("grey@gmai.com", result.get().getEmail());
        assertEquals("123456", result.get().getPassword());
    }
}
