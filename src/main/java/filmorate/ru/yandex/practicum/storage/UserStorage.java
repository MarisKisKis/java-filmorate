package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserStorage {
    public Optional<User> findAll();
    public Optional<User> findUserById(long id);
    public Optional<User> createUser(User user);
    public Optional<User> updateUser(User user);
    void addFriend(Optional<User> user, Optional<User> friend, long friendshipId);
    void deleteFriend(long friendshipId);
    public long findFriendshipId(long userId, long friendId);
    public Optional<User> getAllFriends (long userId);
    public Optional<User> getCommonFriends(long userId, long friendId);
}
