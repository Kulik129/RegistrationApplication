package ru.kulik.registration.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.UserService;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "","This email is already taken");
        }
    }

}
