package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findAll() {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users ");

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователи с идентификатором не найдены.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findUserById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователи с идентификатором не найдены.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> createUser(User user) {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("insert into users (login, name, birthday)" +
                " VALUES (?, ?, ?) ON CONFLICT DO NOTHING", user.getLogin(), user.getName(), user.getBirthday());

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
             user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Добавлен пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Ошибка добавления");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("update users set login=? where user_id = ?",
                user.getLogin(), user.getId());

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Обновлен пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Ошибка добавления");
            return Optional.empty();
        }
    }


    public void addFriend(Optional<User> user, Optional<User> friend, long friendshipId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("update MUTUAL_FRIENDS set FRIENDSHIP_ID=?" +
                "where user_id = ? and friend_id = ?", friendshipId, user.get().getId(), friend.get().getId());
        if (userRows.next()) {
            user = Optional.of(new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday")));

            log.info("Обновлен пользователь: {} {}", user.get().getId(), user.get().getName());

        } else {
            log.info("Ошибка добавления");
        }
    }

    public void deleteFriend(long friendshipId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("delete from MUTUAL_FRIENDS where FRIENDSHIP_ID=?",
                friendshipId);
        if (userRows.next()) {
            log.info("Друг удален");
        } else {
            log.info("Ошибка добавления");
        }
    }

    public long findFriendshipId(long userId, long friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MUTUAL_FRIENDS where user_id=? and friend_id=?",
                userId, friendId);
        if (userRows.next()) {
            log.info("Дружба найдена");
            return userRows.getLong("friendship_id");
        } else {
            log.info("Ошибка добавления");
            return 0;
        }
    }
    public Optional<User> getAllFriends (long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users MUTUAL_FRIENDS where user_id=?", userId);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователи с идентификатором не найдены.");
            return Optional.empty();
        }
    }
    public Optional<User> getCommonFriends(long userId, long friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MUTUAL_FRIENDS where user_id=? and friend_id=?", userId, friendId);
        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователи с идентификатором не найдены.");
            return Optional.empty();
        }
    }
}
