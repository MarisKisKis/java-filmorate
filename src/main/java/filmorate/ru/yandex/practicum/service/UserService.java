package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public User getUser(long userId) throws NotFoundException {
        final User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return user;
    }

    public User save(User user) {
        return userStorage.saveUser(user);
    }

    public void addFriend(long userId, long friendId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (friend == null) {
            throw new NotFoundException("Друг с id " + friendId + " не найден");
        }
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(long userId, long friendId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        if (friend == null) {
            throw new NotFoundException("Друг с id" + friendId + " не найден");
        }
        userStorage.deleteFriend(user, friend);
    }
    public Collection<User> getAllFriends(long userId){
        User user = userStorage.getUser(userId);
        Collection<User> allFriends = userStorage.getAllFriends(userId);
        return allFriends;
    }
    public Collection<User> getCommonFriends(long userId, long friendId) throws NotFoundException {
        User user1 = userStorage.getUser(userId);
        Collection<User> user1Friends = userStorage.getAllFriends(userId);
        User user2 = userStorage.getUser(friendId);
        if (user1 == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (user2 == null) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найден");
        }
        Collection<User> user2Friends = userStorage.getAllFriends(friendId);
        Collection<User> commonFriends = new HashSet<>(user1Friends);
        commonFriends.retainAll(user2Friends);
        return commonFriends;
    }

    public Collection<User> findAll() {
        Collection<User> allUsers;
        allUsers = userStorage.findAll();
        return allUsers;
    }

    public void updateUser(User user) throws NotFoundException {
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + user.getLogin() + " не найден");
        }
        userStorage.updateUser(user);
    }
}
