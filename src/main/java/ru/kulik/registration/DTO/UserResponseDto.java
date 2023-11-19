package ru.kulik.registration.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String email;
    private LocalDateTime registrationDate;
    private LocalDateTime subscriptionEndDate;
    private String role;
    private boolean active;

    public static UserResponseDto fromUserDto(UserDto userDto) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userDto.getId());
        responseDto.setFirstName(userDto.getFirstName());
        responseDto.setLastName(userDto.getLastName());
        responseDto.setDateOfBirth(userDto.getDateOfBirth());
        responseDto.setPhoneNumber(userDto.getPhoneNumber());
        responseDto.setEmail(userDto.getEmail());
        responseDto.setRegistrationDate(userDto.getRegistrationDate());
        responseDto.setSubscriptionEndDate(userDto.getSubscriptionEndDate());
        responseDto.setRole(userDto.getRole().toString());
        responseDto.setActive(userDto.isActive());
        return responseDto;
    }
}
