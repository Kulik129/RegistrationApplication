package ru.kulik.registration.service;

import ru.kulik.registration.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для сервиса управления пользователями.
 */
public interface UserService {
    /**
     * Сохраняет пользователя.
     *
     * @param user Пользователь для сохранения.
     */
    void save(User user);

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    Optional<User> getUserByID(long id);

    /**
     * Получает пользователя по email.
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Получает пользователя по номеру телефона.
     * @param phone номер тел. пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    Optional<User> getUserByPhone(String phone);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    void delete(long id);

    /**
     * Находит пользователей.
     * @return Список всех пользователей или null если не найдены.
     */
    List<User> getAllUsers();
}