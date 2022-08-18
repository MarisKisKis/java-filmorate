package filmorate.ru.yandex.practicum.storage;

import filmorate.ru.yandex.practicum.model.Film;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FilmStorage {
    public List<Integer> findAll();
    public Film findFilmById(long id);
    public Film saveFilm(Film film);
    public Film updateFilm(Film film);
    public void addLike(long filmId, long userId);

    public List<Integer> getTopFilms();

    public boolean deleteLike(long filmId, long userId);
    public List<String> getAllGenres();
    public String findGenreById(int genreId);
    public List<String> getAllMpa();
    public String findMpaById(int ratingId);
}
