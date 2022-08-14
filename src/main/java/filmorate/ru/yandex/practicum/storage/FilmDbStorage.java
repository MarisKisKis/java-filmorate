package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.Film;
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
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> findAll() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films ");

        // обрабатываем результат выполнения запроса
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releaseDate"));

            log.info("Найдены фильмы: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильмы не найдены.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> findFilmById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where film_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date"));

            log.info("Найден пользователь: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> saveFilm(Film film) {
        // выполняем запрос к базе данных.
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("insert into films (name, description, release_date) " +
        "VALUES (?, ?, ?)", film.getName(), film.getDescription(), film.getReleaseDate());

        // обрабатываем результат выполнения запроса
        if (filmRows.next()) {
             film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date"));

            log.info("Найден пользователь: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильм {} добавлен.", film.getName());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        // выполняем запрос к базе данных.
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("update films set DESCRIPTION=? where film_id = ?",
                film.getDescription(), film.getId());

        // обрабатываем результат выполнения запроса
        if (filmRows.next()) {
            film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date"));

            log.info("Найден пользователь: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильм {} добавлен.", film.getName());
            return Optional.empty();
        }
    }

    @Override
    public void addLike(Optional<Film> film, Optional<User> user, long likenessId) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("update film_likes set likeness_id=? where film_id = ?",
                likenessId, film.get().getId(), user.get().getId());

        if (likeRows.next()) {
            log.info("Лайк добавлен");
        } else {
            log.info("Ошибка");
        }
    }

    public long findLikenessId(long userId, long filmId) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("select * from film_likes where user_id=? and film_id=?",
                userId, filmId);
        if (likeRows.next()) {
            log.info("Лайк найден");
            return likeRows.getLong("likeness_id");
        } else {
            log.info("Лайк не найден");
            return 0;
        }
    }

    public void deleteLike(long likenessId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("delete from film_likes where likeness_id=?",
                likenessId);
        if (filmRows.next()) {
            log.info("Лайк удален");
        } else {
            log.info("Ошибка");
        }
    }

}
