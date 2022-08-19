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
import java.util.stream.Collectors;

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
        addLike(film.getId(), 1);
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
    public List<Integer> getTopFilms(int count) {
        ArrayList<Integer> topFilms = new ArrayList<>();
        SqlRowSet topFilmRows = jdbcTemplate.queryForRowSet("select f.id from films f where f.id in " +
                "(select fl.film_id from film_likes fl group by fl.film_id order by count(fl.film_id)) limit  10");

        while (topFilmRows.next()) {
            topFilms.add(topFilmRows.getInt("id"));
        }
        return topFilms;
    }

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

    void  setFilmGenre (Film film) {
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

    void deleteGenre (Film film) {
        String genteSql = "DELETE FROM film_genre where film_id=?";
        jdbcTemplate.update(genteSql, film.getId());
    }
}
