package filmorate.ru.yandex.practicum.storage.impl;

import filmorate.ru.yandex.practicum.model.Genre;
import filmorate.ru.yandex.practicum.storage.dao.GenresDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("genresDaoImpl")
public class GenresDaoImpl implements GenresDao {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        ArrayList<Genre> allGenres = new ArrayList<>();
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select * from genres");
        while (genresRows.next()) {
            Genre genre = new Genre(
                    genresRows.getInt("id"),
                    genresRows.getString("genre_name"));
            genresRows.getInt("id");
            allGenres.add(genre);
            log.info("Жанры получены");
        }
        return allGenres;
    }

    @Override
    public Genre findGenreById(int genreId) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select * from genres where ID=?", genreId);
        if (genresRows.next()) {
            Genre genre = new Genre(
                    genresRows.getInt("id"),
                    genresRows.getString("genre_name"));
            return genre;
        } else {
            log.info("Ошибка");
            return null;
        }
    }
}
