package ru.kulik.registration.service;

import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UpdateInfo;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.request.UserRequest;
import ru.kulik.registration.DTO.response.UserResponse;

import java.util.List;

/**
 * Интерфейс для сервиса управления пользователями.
 */
public interface UserService {

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    UserResponse getUserByID(long id);

    /**
     * Создание нового пользователя.
     *
     * @param user Пользователь для создания.
     */
    UserResponse createUser(UserRequest user);

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    UserResponse getUserByEmail(String email);

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phone номер тел. пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    UserResponse getUserByPhone(String phone);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    void deleteUserByID(long id);

    /**
     * Находит пользователей.
     *
     * @return Список всех пользователей или null если не найдены.
     */
    List<UserResponse> getAllUsers();

    /**
     * Обновление информации о пользователе.
     *
     * @param id       id пользователя.
     * @param userInfo параметры для обновления.
     * @return UserResponse с обновленными данными.
     */
    UserResponse updateUserInfo(long id, UpdateInfo userInfo);

    /**
     * Аутентификация пользователя.
     *
     * @param userAuthenticate параметры для аутентификации.
     * @return UserResponse с обновленными данными.
     */
    UserResponse authenticate(UserAuthenticate userAuthenticate);

    /**
     * Обновление пароля пользователя.
     *
     * @param id             id пользователя.
     * @param updatePassword
     * @return UserResponse с обновленными данными.
     */
    UserResponse updatePassword(long id, UpdatePassword updatePassword);

    /**
     * Обновление окончания даты подписки.
     *
     * @param id       id пользователя.
     * @param dateTime дата и время окончания подписки в формате: гггг-мм-ччTчч:мм:сс
     * @return UserResponse с обновленными данными.
     */
    UserResponse updateSubscription(long id, String dateTime);

    /**
     * Изменение роли пользователя в приложении.
     *
     * @param id    id пользователя.
     * @param param значение
     * @return UserResponse с обновленными данными.
     */
    UserResponse updateRole(long id, boolean param);

    /**
     * Блокировка/ разблокировка пользователя.
     *
     * @param id    id пользователя.
     * @param param значение.
     * @return UserResponse с обновленными данными.
     */
    UserResponse updateActive(long id, boolean param);
}