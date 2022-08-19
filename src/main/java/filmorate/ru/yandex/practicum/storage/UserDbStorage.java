package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
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
    public List<Integer> findAll() {
        ArrayList<Integer> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select id from users ");
            while(userRows.next()) {
                users.add(userRows.getInt("id"));
            }
        return users;
    }

    @Override
    public User findUserById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getDate("birthday"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return user;
        } else {
            log.info("Пользователи с идентификатором не найдены.");
            return null;
        }
    }

    @Override
    public User createUser(User user) {
        String userRows = "insert into users (login, name, EMAIL, birthday)" +
                " VALUES ( ?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder(); //делаем кихолдер
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(userRows, new String[]{"id"});
            pst.setString(1, user.getLogin());
            pst.setString(2, user.getName());
            pst.setString(3, user.getEmail());
            pst.setDate(4, user.getBirthday());
            return pst;
        }, kh);
        user.setId(kh.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String userRows = "update users set login=?,  name=?, EMAIL=?, BIRTHDAY=? " +
                " where id = ?";
         jdbcTemplate.update(userRows,
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
         return user;
    }


    public void addFriend(long userId, long friendId) {
        String userRows = "insert into MUTUAL_FRIENDS (user_id, friend_id) VALUES (?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(userRows, new String[]{"friendship_id"});
            pst.setLong(1, userId);
            pst.setLong(2, friendId);
            return pst;
        }, kh);
    }

    public void deleteFriend(long userId, long friendId) {
        String friendSql = "DELETE FROM MUTUAL_FRIENDS where user_id=? and friend_id=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(friendSql, new String[]{"friendship_id"});
            pst.setLong(1, userId);
            pst.setLong(2, friendId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A user was deleted successfully!");
            }
            return pst;
        });
    }

    public List<Integer> getAllFriends(long userId) {
        ArrayList<Integer> allFriendsIds = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select friend_id from MUTUAL_FRIENDS where user_id=?", userId);
        while (userRows.next()) {
            allFriendsIds.add(userRows.getInt("friend_id"));
        }
        return allFriendsIds;
    }

    public List<Integer> getMutualFriends(long userId, long friendId) {
        ArrayList<Integer> commonIds = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select FRIENDSHIP_ID from MUTUAL_FRIENDS where user_id=? " +
                "and friend_id=?", userId, friendId);
        if (userRows.next()) {
            commonIds.add(userRows.getInt("friendship_id"));
        }
        return commonIds;
    }

    public List<Integer> getCommonFriends(long userId, long friendId) {
        ArrayList<Integer> commonIds = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT distinct (m.FRIEND_id) " +
                "from MUTUAL_FRIENDS as m inner JOIN (SELECT m.FRIEND_id from MUTUAL_FRIENDS as m where user_id = ?) " +
                "on m.friend_ID = m.FRIEND_ID where USER_ID=?", userId, friendId);
        while (userRows.next()) {
            commonIds.add(userRows.getInt("friend_id"));
        }
        return commonIds;
    }
}
