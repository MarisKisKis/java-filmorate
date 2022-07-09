package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    @Test
    void validateID() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setId(-1);
        assertThrows(RuntimeException.class, () -> userController.validateUser(user) );
    }
    @Test
    void validateEmail() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setEmail("pain ful");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user) );
    }
    @Test
    void validateLoginContainsChar() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setLogin("pain ful");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user) );
    }
    @Test
    void validateLoginContainsSpace() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setLogin(" ");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user) );
    }
    @Test
    void validateBirthday() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setBirthday(LocalDate.MAX);
        assertThrows(RuntimeException.class, () -> userController.validateUser(user) );
    }
}
