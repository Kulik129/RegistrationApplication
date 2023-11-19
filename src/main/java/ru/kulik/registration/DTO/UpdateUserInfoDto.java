package ru.kulik.registration.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserInfoDto {
    @NotBlank(message = "Требуется имя.")
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов.")
    private String firstName;
    @NotBlank(message = "Требуется фамилия.")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 3 до 50 символов.")
    private String lastName;
    @NotBlank(message = "Требуется дата рождения.")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Дата рождения должна быть в формате ДД.ММ.ГГГГ.")
    private String dateOfBirth;

}
