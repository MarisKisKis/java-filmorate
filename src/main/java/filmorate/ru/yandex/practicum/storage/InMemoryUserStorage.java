package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.exception.UserValidationException;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private static Integer generatorId = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User saveUser(User user) {
        validateUser(user);
        long id = ++generatorId;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public void validateUser(User user) {
        if ((user.getEmail().equals(" "))||(!user.getEmail().contains("@")||(user.getEmail().contains(" ")))) {
            throw new UserValidationException("E-mail не может быть пустым, с пробелами и должен содержать @!");
        } else if ((user.getLogin().equals(" "))||(user.getLogin().contains(" "))) {
            throw new UserValidationException("Login не может быть пустым или с пробелами!");
        }  else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidationException("Дата рождения не может быть в будущем!");
        } else if ((user.getName().isEmpty())||(user.getName().equals(" "))) {
            user.setName(user.getLogin());
        } else if (user.getId() < 0) {
            throw new UserValidationException("Id не может быть меньше 0");
        }
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(int userId) {
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
}
