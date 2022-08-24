package filmorate.ru.yandex.practicum.storage.impl;

import filmorate.ru.yandex.practicum.model.Mpa;
import filmorate.ru.yandex.practicum.storage.dao.MpaDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("mpaDaoImpl")
public class MpaDaoImpl implements MpaDao {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        ArrayList<Mpa> allMpa = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from RATING");
        while (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("rating_id"),
                    mpaRows.getString("rating_name"));
            allMpa.add(mpa);
            log.info("Рейтинги получены");
        }
        return allMpa;
    }

    @Override
    public Mpa findMpaById(int ratingId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from RATING where RATING_ID=?", ratingId);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt(ratingId),
                    mpaRows.getString("rating_name")
            );
            return mpa;
        } else {
            log.info("Ошибка");
            return null;
        }
    }
}
