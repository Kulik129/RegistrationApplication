package ru.kulik.registration.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.UserServiceImpl;

/**
 * Класс валидатора для объектов User, проверяющий уникальность email и номера телефона.
 */
@Component
public class UserValidator implements Validator {
    private final UserServiceImpl userService;

    public UserValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "", "Этот логин занят.");
        }
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "", "Этот номер телефона занят.");
        }
    }
}
