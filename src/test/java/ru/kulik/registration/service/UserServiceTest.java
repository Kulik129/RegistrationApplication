package ru.kulik.registration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.kulik.registration.DTO.response.UserResponse;
import ru.kulik.registration.exception.UserNotFoundException;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;
    User user = User.builder()
            .id(1)
            .firstName("Tom")
            .lastName("Shelby")
            .email("tom@example.com")
            .phoneNumber("89226665522")
            .password("123456")
            .role(UserRole.USER)
            .active(true)
            .build();

    @Test
    void testGetUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build());

        UserResponse result = userService.getUserByID(user.getId());

        assertEquals("Tom", result.getFirstName());
        assertEquals("Shelby", result.getLastName());
        assertEquals("tom@example.com", result.getEmail());
        assertEquals("123456", result.getPassword());
    }
    @Test
    void testGetUserByIdNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByID(userId));
    }
    @Test
    void testGetByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build());

        UserResponse result = userService.getUserByEmail(user.getEmail());

        assertEquals("Tom", result.getFirstName());
        assertEquals("Shelby", result.getLastName());
        assertEquals("tom@example.com", result.getEmail());
        assertEquals("123456", result.getPassword());
    }
    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserByEmail("nonexistent@example.com"));

        assertEquals("Пользователь с email nonexistent@example.com не найден", exception.getMessage());
    }
    @Test
    void testGetUserByPhone() {
        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build());

        UserResponse result = userService.getUserByPhone(user.getPhoneNumber());

        assertEquals("Tom", result.getFirstName());
        assertEquals("Shelby", result.getLastName());
        assertEquals("tom@example.com", result.getEmail());
        assertEquals("89226665522", result.getPhoneNumber());
    }
    @Test
    void testGetUserByPhone_UserNotFound() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        String phone = "89120009988";

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserByPhone(phone));

        assertEquals("Пользователь с номером " + phone + " не найден", exception.getMessage());
    }
    @Test
    void testDelete() {
        userService.deleteUserByID(user.getId());

        verify(userRepository).deleteById(user.getId());
    }
    @Test
    public void testGetAllUsers() {
        User user1 = User.builder().id(1).firstName("Tom").email("tom@example.com").password("123456").phoneNumber("89251112233").build();
        User user2 = User.builder().id(2).firstName("Artur").email("artur@shelby.com").password("123456").phoneNumber("89252221133").build();

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }
}
