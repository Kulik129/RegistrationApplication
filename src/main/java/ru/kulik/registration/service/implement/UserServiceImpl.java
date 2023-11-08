package ru.kulik.registration.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kulik.registration.model.User;
import ru.kulik.registration.repository.UserRepository;
import ru.kulik.registration.service.UserService;

/**
 * Реализация сервиса пользователей.
 * <p>
 * Этот класс предоставляет методы для сохранения, получения и удаления пользователей.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор класса, инъектирующий зависимость UserRepository.
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
     * @return Объект пользователя, если найден, иначе null.
     */
    @Override
    public User getUser(long id) {
        return userRepository.findById(id).orElse(null);
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
}