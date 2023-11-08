package ru.kulik.registration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private long id;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Дата и время регистрации пользователя в формате "YYYY-MM-DDTHH:MM:SSS+SS:SS".
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    /**
     * Конструктор для создания пользователя с указанным именем и паролем. Дата и время регистрации
     * будут установлены автоматически при создании.
     *
     * @param name     Имя пользователя.
     * @param password Пароль пользователя.
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.registrationDate = new Date();
    }
}