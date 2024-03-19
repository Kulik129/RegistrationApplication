package ru.kulik.registration.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UpdateInfo;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.request.UserRequest;
import ru.kulik.registration.DTO.response.UserResponse;
import ru.kulik.registration.exception.IncorrectPasswordException;
import ru.kulik.registration.exception.UserNotFoundException;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.repository.UserRepository;
import ru.kulik.registration.security.Password;
import ru.kulik.registration.service.mail.EmailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса пользователей.
 * Класс предоставляет методы для сохранения, получения и удаления пользователей.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //regionDI
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailSender emailSender;
    //endregion


    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя, которого нужно получить.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByID(long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }

    /**
     * Создает нового пользователя.
     *
     * @param request Пользователь для создания.
     */
    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        User createUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .registrationDate(LocalDateTime.now())
                .subscriptionEndDate(LocalDateTime.now().plusDays(1))
                .role(UserRole.USER)
                .active(true)
                .build();

        User user = userRepository.save(createUser);
        return modelMapper.map(user, UserResponse.class);
    }

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() ->
                        new UserNotFoundException("Пользователь с email " + email + " не найден", "Not found email"));
    }

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phone номер тел. пользователя.
     * @return Optional, содержащий пользователя, если найден, или пустой, если не найден.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone)
                .map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() ->
                        new UserNotFoundException("Пользователь с номером " + phone + " не найден", "Not found phone"));
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя, которого нужно удалить.
     */
    @Override
    public void deleteUserByID(long id) {
        userRepository.deleteById(id);
    }

    /**
     * Находит всех пользователей.
     *
     * @return Список всех пользователей или null если не найдены.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserResponse updateUserInfo(long id, UpdateInfo updateInfo) {
        Optional<User> updatedUser = userRepository.findById(id);
        updatedUser.ifPresent(user -> {
            user.setFirstName(updateInfo.getFirstName());
            user.setLastName(updateInfo.getLastName());
            user.setDateOfBirth(updateInfo.getDateOfBirth());
            userRepository.save(user);
        });

        return updatedUser
                .map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() ->
                        new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse authenticate(UserAuthenticate data) {
        return userRepository.findByPhoneNumber(data.getPhoneNumber())
                .map(user -> {
                    if (passwordEncoder.matches(data.getPassword(), user.getPassword())) {
                        return modelMapper.map(user, UserResponse.class);
                    } else {
                        throw new IncorrectPasswordException("Неверный пароль","INVALID");
                    }
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь с номером " + data.getPhoneNumber() + " не найден", "Not found id"));
    }


    @Override
    @Transactional
    public UserResponse updatePassword(long id, UpdatePassword data) {
        return userRepository.findById(id)
                .map(user -> {
                    if (passwordEncoder.matches(data.getPassword(), user.getPassword())){
                        user.setPassword(passwordEncoder.encode(data.getPasswordNew()));
                        User save = userRepository.save(user);
                        return modelMapper.map(save, UserResponse.class);
                    } else {
                        throw new IncorrectPasswordException("Неверный пароль","INVALID");
                    }
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }

    @Override
    @Transactional
    public UserResponse updateSubscription(long id, String dateTime) {
        return userRepository.findById(id)
                .map(user -> {
                    LocalDateTime time = LocalDateTime.parse(dateTime);
                    user.setSubscriptionEndDate(time);
                    User save = userRepository.save(user);
                    return modelMapper.map(save, UserResponse.class);
                })
                .orElseThrow(()->  new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }

    @Override
    @Transactional
    public UserResponse updateRole(long id, boolean param) {
        return userRepository.findById(id)
                .map(user -> {
                    if (param) {
                        user.setRole(UserRole.ADMIN);
                        User save = userRepository.save(user);
                        return modelMapper.map(save, UserResponse.class);
                    } else {
                        user.setRole(UserRole.USER);
                        User user1 = userRepository.save(user);
                        return modelMapper.map(user1, UserResponse.class);
                    }
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }

    @Override
    @Transactional
    public UserResponse updateActive(long id, boolean param) {
        return userRepository.findById(id)
                .map(user -> {
                    if (param) {
                        user.setActive(true);
                        User save = userRepository.save(user);
                        return modelMapper.map(save, UserResponse.class);
                    } else {
                        user.setActive(false);
                        User user1 = userRepository.save(user);
                        return modelMapper.map(user1, UserResponse.class);
                    }
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }

    @Override
    @Transactional
    public UserResponse createNewRandomPassword(long id) {
        return userRepository.findById(id)
                .map(user -> {
                    StringBuilder generatePassword = Password.generate();
                    user.setPassword(passwordEncoder.encode(generatePassword));
                    userRepository.save(user);
                    emailSender.sendEmail(user.getEmail(), "Обновление пароля", "Ваш новый пароль: " + generatePassword);
                    return modelMapper.map(user, UserResponse.class);
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден", "Not found id"));
    }
}