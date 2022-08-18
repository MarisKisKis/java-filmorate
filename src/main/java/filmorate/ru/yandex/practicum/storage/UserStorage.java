package filmorate.ru.yandex.practicum.storage;

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
    void addFriend(long user, long friend);
    void deleteFriend(long userId, long friendId);
    public List<Integer> getAllFriends (long userId);
    public List<Integer> getCommonFriends(long userId, long friendId);
    public List<Integer> getMutualFriends(long userId, long friendId);
}
