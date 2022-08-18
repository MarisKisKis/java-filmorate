package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.Genre;
import filmorate.ru.yandex.practicum.model.Mpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                " r.RATING_NAME, r.RATING_ID, ARRAY_AGG(g.id || ':' || g.genre_name) AS genres_id " +
                "from films as f " +
                "left join FILM_GENRE FG on f.ID = FG.FILM_ID " +
                "left join GENRES g on FG.ID = G.ID " +
                "left join RATING r on f.MPA = r.rating_id " +
                "where f.id = ?" +
                "group by f.ID";

        //String sqlQuery = "select f.* , fg.*, r.rating_id from FILMS f, FILM_GENRE fg, RATING r where f.id=? and fg.FILM_ID = ?" +
        //     " and f.mpa=r.RATING_ID";
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
        // String genreQuery = "insert into film_genre (genre_id, film_id) values (?, ?)";
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
            KeyHolder kh2 = new GeneratedKeyHolder();
            for (Genre gener : film.getGenres()) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement pst = connection.prepareStatement(filmGenreQuery, new String[]{"genre_to_film"});
                    pst.setInt(1, gener.getId());
                    pst.setLong(2, film.getId());
                    return pst;
                }, kh2);
            }
        }
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
        return film;
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

    @Override
    public List<Integer> getTopFilms() {
        ArrayList<Integer> topFilms = new ArrayList<>();
        SqlRowSet topFilmRows = jdbcTemplate.queryForRowSet("select * from FILMS f where f.id = (select f2.id from FILMS f2, FILM_LIKES fl where f2.id = fl.FILM_ID" +
                "                                  group by f2.id  order by count(fl.FILM_ID) limit 10)");
        while (topFilmRows.next()) {
        topFilms.add(topFilmRows.getInt("id"));
        }
        return topFilms;
    }

    public boolean deleteLike(long filmId, long userId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("delete from film_likes where film_id = ? and user_id =?",
                filmId, userId);
        return jdbcTemplate.update(String.valueOf(filmRows), "likeness_id") > 0;
    }

    public List<String> getAllGenres() {
        ArrayList<String> allGenres = new ArrayList<>();
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select * from genres");
        if (genresRows.next()) {
            genresRows.getInt("genre_id");
            allGenres.add(genresRows.getString("genre_name"));
            log.info("Жанры получены");
            return allGenres;
        } else {
            log.info("Ошибка");
            return null;
        }
    }

    public String findGenreById(int genreId) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select GENRE_NAME from genres where ID=?", genreId);
        if (genresRows.next()) {
            genresRows.getInt("genre_id");
            log.info("Жанры получены");
            return genresRows.getString("genre_name");
        } else {
            log.info("Ошибка");
            return null;
        }
    }

    public List<String> getAllMpa() {
        ArrayList<String> allMpa = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from RATING");
        while (mpaRows.next()) {
            mpaRows.getInt("rating_id");
            allMpa.add(mpaRows.getString("rating_name"));
            log.info("Жанры получены");
        }
        return allMpa;
    }

    public String findMpaById(int ratingId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select rating_name from RATING where RATING_ID=?", ratingId);
        if (mpaRows.next()) {
            mpaRows.getInt("rating_id");
            log.info("Жанры получены");
            return mpaRows.getString("rating_name");
        } else {
            log.info("Ошибка");
            return null;
        }
    }

    public static Film makeFilm(ResultSet rs, int row) throws SQLException {
        Mpa mpa = new Mpa(
                rs.getInt("rating_id"),
                rs.getString("rating_name"));
        // LinkedHashSet<Genre> genres_id = (LinkedHashSet<Genre>) rs.getArray("genre_id");
        Set<Genre> genres_id = null;
        Object genreFromRow = rs.getArray("id");
        if (genreFromRow != null) {
            genres_id = new HashSet<>();
            Object[] arr = (Object[]) rs.getArray("id").getArray();
            for (Object genre : arr) {
                String[] mapper = genre.toString().split(":");
                if (mapper[1] == null) {
                    mapper[1] = "not found";
                }
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
}
