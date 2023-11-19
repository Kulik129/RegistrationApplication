package ru.kulik.registration.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kulik.registration.DTO.UserDto;
import ru.kulik.registration.exception.UserNotFoundException;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса пользователей.
 * Класс предоставляет методы для сохранения, получения и удаления пользователей.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    /**
     * Сохраняет пользователя.
     *
     * @param userDto Объект пользователя, который должен быть сохранен.
     */
    @Override
    public void save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя, которого нужно получить.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<UserDto> getUserByID(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.map(value -> modelMapper.map(value, UserDto.class));
        } else {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id");
        }
    }

    /**
     * Создает нового пользователя.
     *
     * @param user Пользователь для создания.
     */
    @Override
    public void createUser(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        save(user);
    }

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.map(value -> modelMapper.map(value, UserDto.class));
        } else {
            throw new UserNotFoundException("Пользователь с email " + email + " не найден", "Not found email");
        }
    }

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phone номер тел. пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<UserDto> getUserByPhone(String phone) {
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        if (user.isPresent()) {
            return user.map(value -> modelMapper.map(value, UserDto.class));
        } else {
            throw new UserNotFoundException("Пользователь с номером " + phone + " не найден", "Not found phone");
        }
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя, которого нужно удалить.
     */
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    /**
     * Находит всех пользователей.
     *
     * @return Список всех пользователей или null если не найдены.
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Проверка существования пользователя в базе по email.
     *
     * @param email email пользователя.
     * @return true если пользователь найден.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Проверка существования пользователя в базе по номеру телефона.
     *
     * @param phone тел. пользователя.
     * @return true если пользователь найден.
     */
    public boolean existsByPhoneNumber(String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }
}