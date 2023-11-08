package ru.kulik.registration.service;

import ru.kulik.registration.model.User;

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
     * @return Найденный пользователь или null, если не найден.
     */
    User getUser(long id);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    void delete(long id);
}