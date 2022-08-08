package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static Integer generatorId = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User saveUser(User user) {
        long id = ++generatorId;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(long userId) {
        final User user = users.get(userId);
        return user;
    }
    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }
    public Set<Long> getAllFriendsIds (User user) {
        Set<Long> friends = user.getFriendIds();
        return friends;
    }
    public Collection <User> getAllFriends (long userId) {
        User user = getUser(userId);
        Set<Long> friendsIds = user.getFriendIds();
        Collection <User> friends = new ArrayList<>();
        for (long i : friendsIds) {
            friends.add(getUser((int) i));
        }
        return friends;
    }
}
