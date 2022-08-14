package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.UserDbStorage;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;
    private final UserStorage userStorage;
    private long generatorId;

    @Autowired UserService(UserDbStorage userDbStorage, UserStorage userStorage) {
        this.userDbStorage = userDbStorage;
        this.userStorage = userStorage;
    }

    public Optional<User> getUser(long userId) throws NotFoundException {
        final Optional<User> user = userStorage.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return userDbStorage.findUserById(userId);
    }

    public Optional<User> save(User user) {
        return userStorage.createUser(user);
    }

    public void addFriend(long userId, long friendId) throws NotFoundException {
        Optional<User> user = userStorage.findUserById(userId);
        Optional<User> friend = userStorage.findUserById(friendId);
        long friendshipId = 0;
        friendshipId = generatorId++;
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (friend == null) {
            throw new NotFoundException("Друг с id " + friendId + " не найден");
        }
        userStorage.addFriend(user, friend, friendshipId);
    }

    public void deleteFriend(long userId, long friendId) throws NotFoundException {
        Optional<User> user = userStorage.findUserById(userId);
        Optional<User> friend = userStorage.findUserById(friendId);
        long friendshipId = userStorage.findFriendshipId(userId, friendId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        if (friend == null) {
            throw new NotFoundException("Друг с id" + friendId + " не найден");
        }
        userStorage.deleteFriend(friendshipId);
    }
    public Optional<User> getAllFriends(long userId){
        Optional<User> user = userStorage.findUserById(userId);
        Optional<User> allFriends = userStorage.getAllFriends(userId);
        return allFriends;
    }
    public Optional<User> getCommonFriends(long userId, long friendId) throws NotFoundException {
        Optional<User> commonFriends = userStorage.getCommonFriends(userId, friendId);
        return commonFriends;
    }

    public Optional<User> findAll() {
        Optional<User> allUsers;
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
