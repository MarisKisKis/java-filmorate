package filmorate.ru.yandex.practicum.controller;


import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.exception.ValidationException;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Текущее количество пользователей : {}", userService.findAll().size());
        return userService.findAll();
    }

    @PostMapping
    User createUser(@RequestBody User user) throws NotFoundException {
        validateUser(user);
        userService.save(user);
        log.trace("Пользователь" + user.getName() + " добавлен");
        return user;
    }

    @PutMapping
    User updateUser(@RequestBody User user) throws NotFoundException {
        validateUser(user);
        userService.updateUser(user);
        log.trace("Пользователь " + user.getName() + "обновлен");
        return user;
    }
    @GetMapping("/{userId}")
    User getUser(@PathVariable long userId) throws NotFoundException {
        log.info("Получили пользователя по id ", userId);
        return userService.getUser(userId);
    }
    @GetMapping("/{userId}/friends")
    public Collection<User> getAllFriends(@PathVariable long userId) {
        return userService.getAllFriends(userId);
    }
    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) throws NotFoundException {
        return userService.getCommonFriends(userId, friendId);
    }
    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) throws NotFoundException {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) throws NotFoundException {
        userService.deleteFriend(userId, friendId);
    }
    public void validateUser(User user) throws NotFoundException {
        if ((user.getEmail().equals(" "))||(!user.getEmail().contains("@")||(user.getEmail().contains(" ")))) {
            log.warn("Ошибка e-mail пользователя " + user.getLogin());
            throw new ValidationException("E-mail не может быть пустым, с пробелами и должен содержать @!");
        } else if ((user.getLogin().equals(" "))||(user.getLogin().contains(" "))) {
            log.warn("ошибка login пользователя " + user.getLogin());
            throw new ValidationException("Login не может быть пустым или с пробелами!");
        }  else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("ошибка birthday пользователя " + user.getLogin());
            throw new ValidationException("Дата рождения не может быть в будущем!");
        } else if ((user.getName().isEmpty())||(user.getName().equals(" "))) {
            user.setName(user.getLogin());
            log.warn("name = login  пользователя " + user.getLogin());
        } else if (user.getId() < 0) {
            log.warn("ошибка userId  пользователя " + user.getLogin()); // эта проверка нужна для прохождения тестов,
            // которые ожидают ошибку при отрицательном id
            throw new NotFoundException("Нет такого пользователя");
        }
    }
}
