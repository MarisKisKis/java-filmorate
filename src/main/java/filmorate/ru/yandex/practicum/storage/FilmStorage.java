package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public interface FilmStorage {
    public Collection<Film> findAll();
    public Film saveFilm(Film film);
    public Film updateFilm(Film film);
    public Film getFilm (long filmId);
    public void addLike(Film film, User user);
    public void deleteLike(Film film, User user);
}
