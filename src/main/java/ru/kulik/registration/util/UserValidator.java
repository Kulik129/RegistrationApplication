package ru.kulik.registration.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kulik.registration.model.User;
import ru.kulik.registration.repository.UserRepository;

/**
 * Класс валидатора для объектов User, проверяющий уникальность email и номера телефона.
 */
@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository repository;
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (repository.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "", "Этот логин занят.");
        }
        if (repository.existsByPhoneNumber(user.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "", "Этот номер телефона занят.");
        }
    }
}
