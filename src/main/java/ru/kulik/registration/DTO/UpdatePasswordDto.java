package ru.kulik.registration.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePasswordDto {
    @NotBlank(message = "Требуется пароль.")
    private String password;
    @NotBlank(message = "Требуется пароль.")
    @Size(min = 6, message = "Пароль должен состоять минимум из 6 символов")
    private String passwordNew;
}
