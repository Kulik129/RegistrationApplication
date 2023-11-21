package ru.kulik.registration.security;

import java.util.Random;

/**
 * Генерация случайного пароля
 */
public class Password {
    public static StringBuilder generate() {
        int length = 12;
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int rand = random.nextInt(33, 123);
            char ch = (char) rand;
            password.append(ch);
        }
        return password;
    }
}
