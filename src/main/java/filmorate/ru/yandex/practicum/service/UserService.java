package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.dao.FriendsDao;
import filmorate.ru.yandex.practicum.storage.impl.UserDbStorage;
import filmorate.ru.yandex.practicum.storage.dao.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    @Autowired UserService(UserDbStorage userDbStorage, UserStorage userStorage, FriendsDao friendsDao) {
        this.userDbStorage = userDbStorage;
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
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
        friendsDao.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) throws NotFoundException {
        if (userId <= 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } if (friendId <= 0) {
            throw new NotFoundException("Фильм с id " + friendId + " не найден");
        }
        friendsDao.deleteFriend(userId, friendId);
    }
    public List<User> getAllFriends(long userId){
        List<Integer> allFriendsids = friendsDao.getAllFriends(userId);
        List<User> allFriends = new ArrayList<>();
        for (int i = 0; i < allFriendsids.size(); i++) {
            allFriends.add(userStorage.findUserById(allFriendsids.get(i)));
        }
        return allFriends;
    }
    public List<User> getCommonFriends(long userId, long friendId) throws NotFoundException {
        List<Integer> commonIds = friendsDao.getCommonFriends(userId, friendId);
        List<User> commonFriends = new ArrayList<>();
        for (int i = 0; i < commonIds.size(); i++) {
            commonFriends.add(userStorage.findUserById(commonIds.get(i)));
        }
        return commonFriends;
    }

    public List<User> getMutualFriends(long userId, long friendId) throws NotFoundException {
        List<Integer> mutualIds = friendsDao.getMutualFriends(userId, friendId);
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
