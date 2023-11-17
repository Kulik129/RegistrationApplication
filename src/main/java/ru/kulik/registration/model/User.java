package ru.kulik.registration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Класс, представляющий сущность "Пользователь" в системе.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID cannot be null")
    private long id;

    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Требуется имя.")
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов.")
    @JsonProperty("first_name")
    private String firstName;
    /**
     * Фамилия пользователя.
     */
    @NotBlank(message = "Требуется фамилия.")
    @Size(min = 3, max = 50, message = "Фамилия должна быть от 3 до 50 символов.")
    @JsonProperty("last_name")
    private String lastName;
    /**
     * Дата рождения пользователя.
     */
    @NotBlank(message = "Требуется дата рождения.")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Дата рождения должна быть в формате ДД.ММ.ГГГГ.")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    /**
     * email пользователя
     */
    @NotBlank(message = "Требуется Email")
    @Email(message = "Email должен быть в формате example@test.ru")
    @Column(unique = true)
    private String email;
    /**
     * Номер телефона пользователя.
     */
    @NotBlank(message = "Требуется номер телефона")
    @Pattern(regexp = "^89\\d{9,11}$", message = "Телефон должен быть в формате 89********* и содержать 11 цифр.")
    @Column(unique = true)
    @JsonProperty("phone_number")
    private String phoneNumber;

    /**
     * Пароль пользователя.
     */
    @NotBlank(message = "Требуется пароль.")
    @Size(min = 6, message = "Пароль должен состоять минимум из 6 символов")
    private String password;

    /**
     * Дата и время регистрации пользователя в формате "YYYY-MM-DDTHH:MM:SSS+SS:SS".
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("registration_date")
    private Date registrationDate;

    /**
     * Дата и время окончания подписки в формате "YYYY-MM-DDTHH:MM:SSS+SS:SS".
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("subscription_end_date")
    private Date subscriptionEndDate;

    /**
     * Роль пользователя.
     */
    private UserRole role;

    @Column(columnDefinition = "BIT")
    private boolean active;

    public User(String firstName, String lastName, String dateOfBirth, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phone;
        this.password = password;
    }
}
