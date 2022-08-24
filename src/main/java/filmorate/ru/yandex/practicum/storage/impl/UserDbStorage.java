package filmorate.ru.yandex.practicum.storage.impl;

import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.dao.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

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
}
