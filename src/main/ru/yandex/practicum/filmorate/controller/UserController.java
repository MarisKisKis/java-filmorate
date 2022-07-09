package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    @GetMapping
    public Collection<User> findAll() {
        log.trace("Текущее количество пользователей : {}", users.size());
        return users;
    }

    @PostMapping
    User createUser(@RequestBody User user) throws UserValidationException {
        validateUser(user);
        users.add(user);
        log.trace("Пользователь добавлен, количество пользователей ", users.size());
        return user;
    }

    public void validateUser(yandex.practicum.filmorate.model.User user) {
        if ((user.getEmail().isEmpty())||(!user.getEmail().contains("@")||(user.getEmail().contains(" ")))) {
            log.trace("ошибка e-mail");
            throw new UserValidationException("E-mail не может быть пустым, с пробелами и должен содержать @!");
        } else if ((user.getLogin().isEmpty())||(user.getLogin().contains(" "))) {
            log.trace("ошибка login");
            throw new UserValidationException("Login не может быть пустым или с пробелами!");
        }  else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("ошибка birthday");
            throw new UserValidationException("Дата рождения не может быть в будущем!");
        } else if ((user.getName().isEmpty())||(user.getName().equals(" "))) {
            user.setName(user.getLogin());
            log.trace("name = login");
        } else if (user.getId() < 0) {
            log.trace("ошибка userId");
            throw new UserValidationException("Id не может быть меньше 0");
        } else if (user.getId() == 0) {
            user.setId(1);
        }
    }

    @PutMapping
    yandex.practicum.filmorate.model.User updateUser(@RequestBody yandex.practicum.filmorate.model.User user) throws UserValidationException {
        validateUser(user);
        users.add(user);
        log.trace("Пользователь обновлен");
        users.remove(0);
        return user;
    }
}
