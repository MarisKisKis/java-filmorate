package filmorate.ru.yandex.practicum.storage.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FriendsDao {
    void addFriend(long user, long friend);
    void deleteFriend(long userId, long friendId);
    public List<Integer> getAllFriends (long userId);
    public List<Integer> getCommonFriends(long userId, long friendId);
    public List<Integer> getMutualFriends(long userId, long friendId);
}
