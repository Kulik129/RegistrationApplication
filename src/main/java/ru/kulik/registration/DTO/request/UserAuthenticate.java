package ru.kulik.registration.DTO.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAuthenticate {
    @NotBlank(message = "Требуется номер телефона.")
    private String phoneNumber;
    @NotBlank(message = "Требуется пароль.")
    private String password;
}
