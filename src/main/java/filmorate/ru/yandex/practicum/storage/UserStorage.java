package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    public Collection<User> findAll();
    User saveUser(@RequestBody User user);
    public void validateUser(User user);
    User updateUser(@RequestBody User user);

    public User getUser(int userId);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    public Set<Long> getAllFriendsIds (User user);
}
