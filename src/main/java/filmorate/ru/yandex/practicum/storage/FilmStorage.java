package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.exception.FilmNotFoundException;
import filmorate.ru.yandex.practicum.exception.UserNotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> findAll();
    public Film createFilm(@RequestBody Film film);
    public void validate(Film film);
    public Film updateFilm(@RequestBody Film film);
    public Film getFilm (int filmId);
    public void addLike(Film film, User user);
    public void deleteLike(Film film, User user);

}
