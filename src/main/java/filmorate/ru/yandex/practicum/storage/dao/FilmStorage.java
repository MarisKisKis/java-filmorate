package filmorate.ru.yandex.practicum.storage.dao;

import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.Genre;
import filmorate.ru.yandex.practicum.model.Mpa;
import org.springframework.stereotype.Component;

import java.lang.reflect.GenericArrayType;
import java.util.List;

@Component
public interface FilmStorage {
    public List<Integer> findAll();
    public Film findFilmById(long id);
    public Film saveFilm(Film film);
    public Film updateFilm(Film film);
    public List<Integer> getTopFilms(int count);
}
