package ru.kulik.registration.DTO.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePassword {
    @NotBlank(message = "Требуется пароль.")
    private String password;
    @NotBlank(message = "Требуется пароль.")
    @Size(min = 6, message = "Пароль должен состоять минимум из 6 символов")
    private String passwordNew;
}
