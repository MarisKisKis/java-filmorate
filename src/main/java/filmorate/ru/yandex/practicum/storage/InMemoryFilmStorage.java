package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    final HashMap<Long, Film> films = new HashMap<>();
    private static Integer generatorId = 0;

    @Override
    public Collection<Film> findAll() {

        return films.values();
    }

    @Override
    public Film saveFilm(Film film) {
        long id = ++generatorId;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(long filmId) {
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
