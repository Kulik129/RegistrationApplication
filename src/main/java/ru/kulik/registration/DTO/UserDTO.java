package ru.kulik.registration.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.kulik.registration.model.UserRole;

import java.util.Date;

@Data
@Component
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String password;
    private String passwordNew;
    private Date registrationDate;
    private Date subscriptionEndDate;
    private UserRole role;
    private boolean active;

}
