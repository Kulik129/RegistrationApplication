package ru.kulik.registration.service;

import ru.kulik.registration.DTO.UserDto;

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
    void save(UserDto user);

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    Optional<UserDto> getUserByID(long id);

    /**
     * Создание нового пользователя.
     * @param user Пользователь для создания.
     */
    void createUser(UserDto user);

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    Optional<UserDto> getUserByEmail(String email);

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phone номер тел. пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    Optional<UserDto> getUserByPhone(String phone);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    void delete(long id);

    /**
     * Находит пользователей.
     *
     * @return Список всех пользователей или null если не найдены.
     */
    List<UserDto> getAllUsers();
}