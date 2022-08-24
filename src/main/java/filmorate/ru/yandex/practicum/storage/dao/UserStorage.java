package filmorate.ru.yandex.practicum.storage.dao;

import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {
    public List<Integer> findAll();
    public User findUserById(long id);
    public User createUser(User user);
    public User updateUser(User user);
}
