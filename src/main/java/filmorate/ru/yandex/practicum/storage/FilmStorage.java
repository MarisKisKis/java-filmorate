package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface FilmStorage {
    public Optional<Film> findAll();
    public Optional<Film> findFilmById(long id);
    public Optional<Film> saveFilm(Film film);
    public Optional<Film> updateFilm(Film film);
    public void addLike(Optional<Film> film, Optional<User> user, long likenessId);
    public void deleteLike(long likenessId);
    public long findLikenessId(long userId, long filmId);
}
