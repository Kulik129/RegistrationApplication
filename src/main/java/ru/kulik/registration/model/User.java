package ru.kulik.registration.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс, представляющий сущность "Пользователь" в системе.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phoneNumber;
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime subscriptionEndDate;
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
    public User(String firsName, String lastName, String email, String password) {
        this.firstName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
