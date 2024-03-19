package ru.kulik.registration.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.response.UserResponse;
import ru.kulik.registration.service.mail.EmailSender;
import ru.kulik.registration.service.UserServiceImpl;

import java.util.Optional;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class MailAspect {
    private final EmailSender emailSender;
    private final UserServiceImpl userService;

    @Pointcut(value = "execution(* ru.kulik.registration.service.UserServiceImpl.createUser(..))")
    public void sendMailCreateUser() {}

    @Pointcut(value = "execution(* ru.kulik.registration.service.UserServiceImpl.updatePassword(..))")
    public void sendMailUpdatePassword() {}

    @Pointcut(value = "execution(* ru.kulik.registration.service.UserServiceImpl.updateUserInfo(..))")
    public void sendMailUpdateInfo() {}

    @AfterReturning(value = "sendMailCreateUser()", returning = "response")
    public void sendMessageHello(UserResponse response) {
        String firstName = response.getFirstName();
        emailSender.sendEmail(
                response.getEmail(),
                "Добро пожаловать!",
                firstName + " Приветствуем вас!");
    }

    @AfterReturning(value = "sendMailUpdatePassword()", returning = "response")
    public void sendMessageUpdatePassword(JoinPoint joinPoint, UserResponse response) {
        UpdatePassword updatePassword = (UpdatePassword) joinPoint.getArgs()[1];
        String passwordNew = updatePassword.getPasswordNew();
        emailSender.sendEmail(
                response.getEmail(),
                "Смена пароля к вашему Личному кабинету",
                "Приветствуем вас!" + "\n" + "Ваш новый пароль: " + passwordNew);
    }

    @AfterReturning(value = "sendMailUpdateInfo()", returning = "response")
    public void updateUserInfo(UserResponse response) {
        String firstName = response.getFirstName();
        String lastName = response.getLastName();
        String dateOfBirth = response.getDateOfBirth();

        emailSender.sendEmail(
                response.getEmail(),
                "Обновление данных",
                "Ваши данные обновлены: \n" +
                        "Изменено имя на: " + firstName + "\n" +
                        "Изменена фамилия на: " + lastName + "\n" +
                        "Изменена дата рождения на: " + dateOfBirth + "\n");
    }

    @AfterThrowing(value = "execution(* ru.kulik.registration.service.UserServiceImpl.authenticate(..))")
    public void invalidPassword(JoinPoint joinPoint) {
        UserAuthenticate authenticate = (UserAuthenticate) joinPoint.getArgs()[0];
        UserResponse user = userService.getUserByPhone(authenticate.getPhoneNumber());
        Optional<UserResponse> userResponse = Optional.of(user);
        userResponse.ifPresent(it -> emailSender.sendEmail(
                it.getEmail(),
                "Ошибка входа",
                "Очень жаль, что у вас возникли проблемы со входом на платформу. Мы получили уведомление что введен не правильный пароль. Если это были не вы, рекомендуем сменить пароль."));
    }
}