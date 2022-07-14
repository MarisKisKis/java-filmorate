package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.UserNotFoundException;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public User getUser(int userId) throws UserNotFoundException {
        final User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return user;
    }

    public User save(User user) {
        return userStorage.saveUser(user);
    }

    public void addFriend(int userId, int friendId) throws UserNotFoundException {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (friend == null) {
            throw new UserNotFoundException("Друг с id " + friendId + " не найден");
        }
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(int userId, int friendId) throws UserNotFoundException {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id" + userId + " не найден");
        }
        if (friend == null) {
            throw new UserNotFoundException("Друг с id" + friendId + " не найден");
        }
        userStorage.deleteFriend(user, friend);
    }
    public Set<Long> getCommonFriends(int userId1, int userId2) throws UserNotFoundException {
        User user1 = userStorage.getUser(userId1);
        Set<Long> user1Friends = user1.getFriendIds();
        User user2 = userStorage.getUser(userId2);
        if (user1 == null) {
            throw new UserNotFoundException("Пользователь с id " + userId1 + " не найден");
        }
        if (user2 == null) {
            throw new UserNotFoundException("Пользователь с id " + userId2 + " не найден");
        }
        Set<Long> user2Friends = user2.getFriendIds();
        Set<Long> commonFriends = new HashSet<>(user1Friends);
        commonFriends.retainAll(user2Friends);
        return commonFriends;
    }
}
