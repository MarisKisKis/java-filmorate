package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.exception.FilmNotFoundException;
import filmorate.ru.yandex.practicum.exception.FilmValidationException;
import filmorate.ru.yandex.practicum.exception.UserNotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private static Integer generatorId = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        long id = ++generatorId;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public void validate(Film film) {
        if (film.getName().isEmpty()) {
            throw new FilmValidationException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            throw new FilmValidationException("Максимальная длина описания — 200 символов!");
        } else if ((film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))) {
            throw new FilmValidationException("Фильм не может быть раньше 28.12.1895");
        } else if (film.getDuration() <= 0) {
            throw new FilmValidationException("Длительность должна быть положительной");
        } else if (film.getId() < 0) {
            throw new FilmValidationException("Id не может быть меньше 0");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(int filmId) {
        final Film film = films.get(filmId);
        return film;
    }

    @Override
    public void addLike(Film film, User user) {
        Set<Long> filmLikes = film.getLikes();
        filmLikes.add(user.getId());
        film.setLikes(filmLikes);
    }

    public void deleteLike(Film film, User user) {
        Set<Long> filmLikes = film.getLikes();
        filmLikes.remove(user.getId());
        film.setLikes(filmLikes);
    }
}
