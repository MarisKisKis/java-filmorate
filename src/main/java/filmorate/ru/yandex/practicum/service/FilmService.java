package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.FilmStorage;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FilmService {
    @Autowired
    UserStorage userStorage;
    @Autowired
    FilmStorage filmStorage;

    public Film getFilm(long filmId) throws NotFoundException {
        if (filmId < 0) {
            throw new NotFoundException("Не найден фильм с id" + filmId);
        }
        final Film film = filmStorage.getFilm(filmId);
        return film;
    }
    public void addLike(long userId, long filmId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.addLike(film, user);
    }

    public void deleteLike(long userId, long filmId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.deleteLike(film, user);
    }

    public Collection<Film> getTopFilms(long count){
     //   List<Film> films = (List<Film>) filmStorage.findAll();
      //  films.sort(Comparator.comparingInt((Film f) -> f.getLikes().size()));
        Collection <Film> top10Films = filmStorage.findAll();
                //new ArrayList<>((int) count);
      //  for (Film film : films) {
     //       top10Films.add(film);
     //   }
        return top10Films;
    }

    public Collection<Film> findAllFilms() {
        Collection <Film> allFilms;
        allFilms = filmStorage.findAll();
        return allFilms;
    }

    public Film save(Film film) {
        return filmStorage.saveFilm(film);
    }

    public void updateFilm(Film film) throws NotFoundException {
        if (film == null) {
            throw new NotFoundException("Фильм с id " + film + " не найден");
        }
        filmStorage.updateFilm(film);
    }
}
