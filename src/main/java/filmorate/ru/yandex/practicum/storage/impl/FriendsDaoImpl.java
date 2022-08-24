package filmorate.ru.yandex.practicum.storage.impl;

import filmorate.ru.yandex.practicum.storage.dao.FriendsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("friendsDaoImpl")
public class FriendsDaoImpl implements FriendsDao {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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

    @Override
    public List<Integer> getAllFriends(long userId) {
        ArrayList<Integer> allFriendsIds = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select friend_id from MUTUAL_FRIENDS where user_id=?", userId);
        while (userRows.next()) {
            allFriendsIds.add(userRows.getInt("friend_id"));
        }
        return allFriendsIds;
    }

    @Override
    public List<Integer> getMutualFriends(long userId, long friendId) {
        ArrayList<Integer> commonIds = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select FRIENDSHIP_ID from MUTUAL_FRIENDS where user_id=? " +
                "and friend_id=?", userId, friendId);
        if (userRows.next()) {
            commonIds.add(userRows.getInt("friendship_id"));
        }
        return commonIds;
    }

    @Override
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
