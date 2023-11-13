package ru.kulik.registration.model;

import jakarta.persistence.*;
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
    private long id;

    /**
     * Имя пользователя.
     */
    private String name;
    /**
     * Фамилия пользователя.
     */
    private String surname;
    /**
     * Дата рождения пользователя.
     */
    private String dateOfBirth;
    /**
     * email пользователя
     */
    private String email;
    /**
     * Номер телефона пользователя.
     */
    private String phoneNumber;

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
     * Конструктор для создания пользователя с указанным именем, фамилией, датой рождения, email и паролем. Дата и время регистрации
     * будут установлены автоматически при создании.
     *
     * @param name        Имя пользователя.
     * @param surname     Фамилия пользователя.
     * @param dateOfBirth Дата рождения пользователя.
     * @param email       email пользователя.
     * @param password    Пароль пользователя.
     */
    public User(String name, String surname, String dateOfBirth, String email,String phoneNumber, String password) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registrationDate = new Date();
    }
}
