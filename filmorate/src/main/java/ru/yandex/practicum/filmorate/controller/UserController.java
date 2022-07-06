package java.ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final List<Post> users = new ArrayList<>();
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Текущее количество пользователей : {}", users.size());
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws UserAlreadyExistException {
        log.trace("Пользователь добавлен", users.size());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws UserNotFoundException {
        return userService.updateUser(user);
    }

    @GetMapping("/user/{userMail}")
    public User getUser(@PathVariable("userMail") String userMail) throws InvalidEmailException {
        return userService.findUserByEmail(userMail);
    }
}
