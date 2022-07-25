package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.Set;

@Component
public interface UserStorage {
    public Collection<User> findAll();
    User saveUser(User user);
    User updateUser(User user);

    public User getUser(long userId);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    public Set<Long> getAllFriendsIds (User user);
    public Collection <User> getAllFriends (long userId);
}
