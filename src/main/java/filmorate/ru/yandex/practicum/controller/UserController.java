package filmorate.ru.yandex.practicum.controller;


import filmorate.ru.yandex.practicum.exception.UserValidationException;
import filmorate.ru.yandex.practicum.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();
    private static Integer generatorId = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Текущее количество пользователей : {}", users.size());
        return users.values();
    }

    @PostMapping
    User createUser(@RequestBody User user) {
        validateUser(user);
        long id = ++generatorId;
        user.setId(id);
        users.put(id, user);
        log.trace("Пользователь" + user.getName() + " добавлен");
        return user;
    }

    public void validateUser(User user) {
        if ((user.getEmail().equals(" "))||(!user.getEmail().contains("@")||(user.getEmail().contains(" ")))) {
            log.warn("Ошибка e-mail пользователя " + user.getLogin());
            throw new UserValidationException("E-mail не может быть пустым, с пробелами и должен содержать @!");
        } else if ((user.getLogin().equals(" "))||(user.getLogin().contains(" "))) {
            log.warn("ошибка login пользователя " + user.getLogin());
            throw new UserValidationException("Login не может быть пустым или с пробелами!");
        }  else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("ошибка birthday пользователя " + user.getLogin());
            throw new UserValidationException("Дата рождения не может быть в будущем!");
        } else if ((user.getName().isEmpty())||(user.getName().equals(" "))) {
            user.setName(user.getLogin());
            log.warn("name = login  пользователя " + user.getLogin());
        } else if (user.getId() < 0) {
            log.warn("ошибка userId  пользователя " + user.getLogin()); // эта проверка нужна для прохождения тестов,
            // которые ожидают ошибку при отрицательном id
            throw new UserValidationException("Id не может быть меньше 0");
        }
    }

    @PutMapping
    User updateUser(@RequestBody User user) {
        validateUser(user);
        users.put(user.getId(), user);
        log.trace("Пользователь " + user.getName() + "обновлен");
        return user;
    }
}
