package ru.kulik.registration.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kulik.registration.model.UserRole;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    @NotNull(message = "ID cannot be null")
    private long id;
    @NotBlank(message = "Требуется имя.")
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов.")
    private String firstName;
    @NotBlank(message = "Требуется фамилия.")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 3 до 50 символов.")
    private String lastName;
    @NotBlank(message = "Требуется дата рождения.")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Дата рождения должна быть в формате ДД.ММ.ГГГГ.")
    private String dateOfBirth;
    @NotBlank(message = "Требуется Email")
    @Email(message = "Email должен быть в формате example@test.ru")
    private String email;
    @NotBlank(message = "Требуется номер телефона")
    @Pattern(regexp = "^89\\d{9,11}$", message = "Телефон должен быть в формате 89********* и содержать 11 цифр.")
    @Size(min = 11, max = 11, message = "Телефон должен быть в формате 89********* и содержать 11 цифр.")
    private String phoneNumber;
    @NotBlank(message = "Требуется пароль.")
    @Size(min = 6, message = "Пароль должен состоять минимум из 6 символов")
    private String password;
    private LocalDateTime registrationDate;
    private LocalDateTime subscriptionEndDate;
    private UserRole role;
    private boolean active;

    public UserDto(String firstName, String lastName, String dateOfBirth, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    public UserDto(String firsName, String lastName, String email, String password) {
        this.firstName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

}
