package filmorate.ru.yandex.practicum.storage.impl;

import filmorate.ru.yandex.practicum.storage.dao.LikesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
@Qualifier("likesDaoImpl")
public class LikesDaoImpl implements LikesDao {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public LikesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String likeSql = "DELETE FROM film_likes where film_id=? and user_id=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(likeSql, new String[]{"likeness_id"});
            pst.setLong(1, filmId);
            pst.setLong(2, userId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A user was deleted successfully!");
            }
            return pst;
        });
    }

    @Override
    public void addLike(long filmId, long userId) {
        String likeRows = "insert into film_likes (user_id, film_id) VALUES (?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(likeRows, new String[]{"likeness_id"});
            pst.setLong(1, userId);
            pst.setLong(2, filmId);
            return pst;
        }, kh);
    }
}
