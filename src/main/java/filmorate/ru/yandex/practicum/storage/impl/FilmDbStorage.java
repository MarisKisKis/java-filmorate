package filmorate.ru.yandex.practicum.storage.impl;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.Genre;
import filmorate.ru.yandex.practicum.model.Mpa;
import filmorate.ru.yandex.practicum.storage.dao.FilmStorage;
import filmorate.ru.yandex.practicum.storage.dao.LikesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, LikesDao likesDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.likesDao = likesDao;
    }

    @Override
    public List<Integer> findAll() {
        ArrayList<Integer> filmIds = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select id from films ");
        while (filmRows.next()) {
            filmIds.add(filmRows.getInt("id"));
        }
        return filmIds;
    }

    @Override
    public Film findFilmById(long id) {
        String sqlQuery = "select f.*," +
                " r.rating_name, r.RATING_ID, ARRAY_AGG(g.id || ':' || g.genre_name) AS genres_id " +
                "from films as f " +
                "left join FILM_GENRE FG on f.ID = FG.FILM_ID " +
                "left join GENRES g on FG.ID = G.ID " +
                "left join RATING r on f.MPA = r.rating_id " +
                " where f.id = ? " +
                "group by f.ID";

        List<Film> filmRows = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        if (filmRows.size() != 1) {
            throw new NotFoundException();
        }
        return filmRows.get(0);
    }

    @Override
    public Film saveFilm(Film film) {

        String filmQuery = "insert into films (name, release_date, description, duration, mpa) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder kh1 = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(filmQuery, new String[]{"id"});
            pst.setString(1, film.getName());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                pst.setNull(2, Types.DATE);
            } else {
                pst.setDate(2, Date.valueOf(releaseDate));
            }
            pst.setString(3, film.getDescription());
            pst.setInt(4, film.getDuration());
            pst.setInt(5, film.getMpa().getId());
            return pst;
        }, kh1);
        film.setId(kh1.getKey().longValue());
        if (film.getGenres() != null) {
            String filmGenreQuery = "insert into film_genre (id, film_id)" +
                    "VALUES (?, ?)";

            for (Genre gener : film.getGenres()) {
                jdbcTemplate.update(filmGenreQuery, gener.getId(), film.getId());
            }
        }
        likesDao.addLike(film.getId(), 1);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String filmRows = "update films set name=?, DESCRIPTION=?, DURATION=?, release_date=?, mpa=? where id = ?";
        jdbcTemplate.update(filmRows,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        log.info("Обновлен фильм: {} {}", film.getId(), film.getName());
        deleteGenre(film);
        setFilmGenre(film);
        return film;
    }

    @Override
    public List<Integer> getTopFilms(int count) {
        ArrayList<Integer> topFilms = new ArrayList<>();
        SqlRowSet topFilmRows = jdbcTemplate.queryForRowSet("select f.id from films f where f.id in " +
                "(select fl.film_id from film_likes fl group by fl.film_id order by count(fl.film_id)) limit  10");

        while (topFilmRows.next()) {
            topFilms.add(topFilmRows.getInt("id"));
        }
        return topFilms;
    }

    public static Film makeFilm(ResultSet rs, int row) throws SQLException {
        Mpa mpa = new Mpa(
                rs.getInt("rating_id"),
                rs.getString("rating_name"));
        LinkedHashSet<Genre> genres_id = null;
        // Object genreFromRow = rs.getArray("genres_id");
        Object[] arr = (Object[]) rs.getArray("genres_id").getArray();
        if (arr[0] != null) {
            genres_id = new LinkedHashSet<>();
            for (Object genre : arr) {
                String[] mapper = genre.toString().split(":");
                genres_id.add(new Genre(Integer.parseInt(mapper[0]), mapper[1]));
            }
        }
        LocalDate date = rs.getDate("release_date").toLocalDate();
        Film film = new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                date,
                rs.getInt("duration"),
                mpa,
                genres_id);
        return film;
    }

    private void  setFilmGenre (Film film) {
        if ((film.getGenres() == null) || (film.getGenres().isEmpty())) {
            String genteSql = "DELETE FROM film_genre where film_id=?";
            jdbcTemplate.update(genteSql, film.getId());
        } else {
            String filmGenreQuery = "insert into film_genre (id, film_id)" +
                    "VALUES (?, ?)";
            for (Genre gener : film.getGenres()) {
                jdbcTemplate.update(filmGenreQuery, gener.getId(), film.getId());
            }
        }
    }

    private void deleteGenre (Film film) {
        String genteSql = "DELETE FROM film_genre where film_id=?";
        jdbcTemplate.update(genteSql, film.getId());
    }
}
