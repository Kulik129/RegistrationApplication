package ru.kulik.registration.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kulik.registration.DTO.request.UpdateInfo;
import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.request.UserRequest;
import ru.kulik.registration.DTO.response.UserResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserServiceImpl userService;
    private static final String KEY = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";
    private static final int TOKEN_VALIDATION_DAYS = 10;

    /**
     * Генерация JWT token.
     * @param subject тема токена.
     * @param user пользователь данные которого будут зашифрованы в токен.
     * @param tokenValidationDays время жизни JWT token'а.
     * @return JWT token с данными.
     */
    public String generateToken(String subject, UserResponse user, int tokenValidationDays) {
        long expirationTimeInMilliseconds = tokenValidationDays * 24 * 60 * 60 * 1000L;

        Date now = new Date();
        Date experationDate = new Date(now.getTime() + expirationTimeInMilliseconds);

        return Jwts.builder()
                .setSubject(subject)
                .claim("ID", user.getId())
                .claim("Role", user.getRole())
                .claim("FirstName", user.getFirstName())
                .claim("LastName", user.getLastName())
                .claim("Email", user.getEmail())
                .claim("PhoneNumber", user.getPhoneNumber())
                .claim("DateOfBirth", user.getDateOfBirth())
                .claim("RegistrationDate", dateTimeFormattedString(user.getRegistration()))
                .claim("SubscriptionEndDate", dateTimeFormattedString(user.getSubscriptionEnd()))
                .claim("Active", user.isActive())
                .setIssuedAt(now)
                .setExpiration(experationDate)
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }

    /**
     * Метод для получения оставшегося времени действия JWT token.
     * @param jwtToken JWT token.
     * @return оставшееся время жизни токена в миллисекундах.
     */
    public long getRemainingTime(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        Date experationDate = claims.getExpiration();
        Date now = new Date();

        return experationDate.getTime() - now.getTime();
    }

    private String dateTimeFormattedString(LocalDateTime dateTime) {
        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = dateTime;
        String formattedString = ldt.format(CUSTOM_FORMATTER);
        return formattedString;
    }
    /**
     * Генерация JWT token'а по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return JWT token с данными.
     */
    public String getUserByID(long id) {
        return generateToken("Пользователь получен по ID.", userService.getUserByID(id), TOKEN_VALIDATION_DAYS);
    }
    /**
     * Генерация JWT token'а для нового пользователя.
     *
     * @return JWT token с данными.
     */
    public String createUser(UserRequest user) {
        return generateToken("Регистрация нового пользователя.", userService.createUser(user), TOKEN_VALIDATION_DAYS);
    }
    /**
     * Генерация JWT token'а по email.
     *
     * @param email email пользователя.
     * @return JWT token с данными.
     */
    public String getUserByEmail(String email) {
        return generateToken("Пользователь получен по email", userService.getUserByEmail(email), TOKEN_VALIDATION_DAYS);
    }
    /**
     * Генерация JWT token'а по номеру телефона.
     *
     * @param phone номер тел. пользователя.
     * @return JWT token с данными.
     */
    public String getUserByPhone(String phone) {
        return generateToken("Пользователь получен по телефону.", userService.getUserByPhone(phone), TOKEN_VALIDATION_DAYS);
    }
    /**
     * Генерация JWT token'а для обновления информации о пользователе.
     *
     * @param id       id пользователя.
     * @param userInfo параметры для обновления.
     * @return JWT token с обновленными данными.
     */
    public String updateUserInfo(long id, UpdateInfo userInfo) {
        return generateToken("Обновлена информация о пользователе.", userService.updateUserInfo(id, userInfo), TOKEN_VALIDATION_DAYS);
    }
    /**
     * Генерация JWT token'а для аутентификации пользователя.
     *
     * @param userAuthenticate параметры для аутентификации.
     * @return JWT token с данными.
     */
    public String authenticate(UserAuthenticate userAuthenticate) {
        return generateToken("Пользователь аутентифицирован.", userService.authenticate(userAuthenticate), TOKEN_VALIDATION_DAYS);
    }

    /**
     * Генерация JWT token'а для обновления пароля пользователя.
     *
     * @param id             id пользователя.
     * @param updatePassword новый и старый пароль.
     * @return JWT token с обновленными данными.
     */
    public String updatePassword(long id, UpdatePassword updatePassword) {
        return generateToken("Обновление пароля.", userService.updatePassword(id, updatePassword), TOKEN_VALIDATION_DAYS);
    }

    /**
     * Генерация JWT token'а обновления окончания даты подписки.
     *
     * @param id       id пользователя.
     * @param dateTime дата и время окончания подписки в формате: гггг-мм-ччTчч:мм:сс
     * @return JWT token с обновленными данными.
     */
    public String updateSubscription(long id, String dateTime) {
        return generateToken("Изменение даты окончания подписки.", userService.updateSubscription(id, dateTime), TOKEN_VALIDATION_DAYS);
    }

    /**
     * Генерация JWT token'а для изменения роли пользователя в приложении.
     *
     * @param id    id пользователя.
     * @param param значение
     * @return JWT token с обновленными данными.
     */
    public String updateRole(long id, boolean param) {
        return generateToken("Назначение роли.", userService.updateRole(id, param), TOKEN_VALIDATION_DAYS);
    }

    /**
     * Генерация JWT token'а для блокировки/ разблокировки пользователя.
     *
     * @param id    id пользователя.
     * @param param значение.
     * @return JWT token с обновленными данными.
     */
    public String updateActive(long id, boolean param) {
        return generateToken("Обновление доступа.", userService.updateActive(id, param), TOKEN_VALIDATION_DAYS);
    }
}
