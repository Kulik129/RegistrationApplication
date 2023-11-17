package ru.kulik.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kulik.registration.exception.InvalidPasswordException;
import ru.kulik.registration.exception.UserNotFoundException;
import ru.kulik.registration.model.User;
import ru.kulik.registration.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса пользователей.
 * <p>
 * Этот класс предоставляет методы для сохранения, получения и удаления пользователей.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор класса, инжектирующий зависимость UserRepository.
     *
     * @param userRepository  Репозиторий пользователей, необходимый для выполнения операций с пользователями.
     * @param passwordEncoder
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Сохраняет пользователя.
     *
     * @param user Объект пользователя, который должен быть сохранен.
     */
    @Override
    public void save(User user) {
        if (user.getPassword().length() > 5) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException("Длинна пароля меньше 6 символов.", "Invalid password");
        }
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя, которого нужно получить.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<User> getUserByID(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id");
        }
    }

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user;
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
    public Optional<User> getUserByPhone(String phone) {
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        if (user.isPresent()) {
            return user;
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
     * Находит пользователей.
     *
     * @return Список всех пользователей или null если не найдены.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
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