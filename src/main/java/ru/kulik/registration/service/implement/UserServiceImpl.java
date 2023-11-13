package ru.kulik.registration.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kulik.registration.model.User;
import ru.kulik.registration.repository.UserRepository;
import ru.kulik.registration.service.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса пользователей.
 *
 * Этот класс предоставляет методы для сохранения, получения и удаления пользователей.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор класса, инжектирующий зависимость UserRepository.
     *
     * @param userRepository Репозиторий пользователей, необходимый для выполнения операций с пользователями.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сохраняет пользователя.
     *
     * @param user Объект пользователя, который должен быть сохранен.
     */
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя, которого нужно получить.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);
    }

    /**
     * Получает пользователя по email.
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Получает пользователя по номеру телефона.
     * @param phone номер тел. пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone);
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
}