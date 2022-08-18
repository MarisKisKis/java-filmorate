package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.UserDbStorage;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;
    private final UserStorage userStorage;

    @Autowired UserService(UserDbStorage userDbStorage, UserStorage userStorage) {
        this.userDbStorage = userDbStorage;
        this.userStorage = userStorage;
    }

    public User getUser(long userId) throws NotFoundException {
        if (userId <= 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        User user = userStorage.findUserById(userId);
        return userDbStorage.findUserById(userId);
    }

    public User save(User user) {
          return userStorage.createUser(user);
    }

    public void addFriend(long userId, long friendId) throws NotFoundException {
        if (userId <= 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (friendId <= 0) {
            throw new NotFoundException("Друг с id " + friendId + " не найден");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) throws NotFoundException {
        if (userId <= 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } if (friendId <= 0) {
            throw new NotFoundException("Фильм с id " + friendId + " не найден");
        }
        userStorage.deleteFriend(userId, friendId);
    }
    public List<User> getAllFriends(long userId){
        List<Integer> allFriendsids = userStorage.getAllFriends(userId);
        List<User> allFriends = new ArrayList<>();
        for (int i = 0; i < allFriendsids.size(); i++) {
            allFriends.add(userStorage.findUserById(allFriendsids.get(i)));
        }
        return allFriends;
    }
    public List<User> getCommonFriends(long userId, long friendId) throws NotFoundException {
        List<Integer> commonIds = userStorage.getCommonFriends(userId, friendId);
        List<User> commonFriends = new ArrayList<>();
        for (int i = 0; i < commonIds.size(); i++) {
            commonFriends.add(userStorage.findUserById(commonIds.get(i)));
        }
        return commonFriends;
    }

    public List<User> getMutualFriends(long userId, long friendId) throws NotFoundException {
        List<Integer> mutualIds = userStorage.getMutualFriends(userId, friendId);
        List<User> mutualFriends = new ArrayList<>();
        for (int i = 0; i < mutualIds.size(); i++) {
            mutualFriends.add(userStorage.findUserById(mutualIds.get(i)));
        }
        return mutualFriends;
    }

    public List<User> findAll() {
        List<Integer> allUsersIds;
        allUsersIds = userStorage.findAll();
        ArrayList<User> allUsers = new ArrayList<>();
        for (int i = 0; i < allUsersIds.size(); i++) {
            allUsers.add(userStorage.findUserById(allUsersIds.get(i)));
        }
        return allUsers;
    }

    public User updateUser(User user) throws NotFoundException {
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
      } else {
            userStorage.updateUser(user);
        }
        return user;
    }
}
