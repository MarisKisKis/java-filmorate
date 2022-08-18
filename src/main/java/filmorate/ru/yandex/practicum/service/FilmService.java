package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.FilmStorage;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService (UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public List<Film> findAllFilms() {
        List<Integer> allFilmIds = filmStorage.findAll();
        List<Film> allFilms = new ArrayList<>();
        for (int i = 0; i < allFilmIds.size(); i++) {
            allFilms.add(filmStorage.findFilmById(allFilmIds.get(i)));
        }
        return allFilms;
    }

    public Film getFilm(long filmId) throws NotFoundException {
        if (filmId < 0) {
            throw new NotFoundException("Не найден фильм с id" + filmId);
        }
        Film film = filmStorage.findFilmById(filmId);
        return film;
    }

    public void addLike(long userId, long filmId) throws NotFoundException {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.addLike(userId, filmId);
    }

    public void deleteLike(long userId, long filmId) throws NotFoundException {
        if (userId <= 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } if (filmId <= 0) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.deleteLike(userId, filmId);
    }


    public List<Film> getTopFilms(int count) {
        List<Integer> top10FilmsIds = filmStorage.getTopFilms();
        if ((top10FilmsIds.size() < 10) && (count == 10)) {
            count = top10FilmsIds.size();
        } else {
            count = count;
        }
        List<Film> topCountFilms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            topCountFilms.add(filmStorage.findFilmById(top10FilmsIds.get(i)));
        }

        return topCountFilms;
    }

    public Film save(Film film) {
        return filmStorage.saveFilm(film);
    }

    public void updateFilm(Film film) throws NotFoundException {
        if (film == null) {
            throw new NotFoundException("Фильм" + film + " не найден");
        }
        filmStorage.updateFilm(film);
    }

    public List<String> getAllGenres() {
       return filmStorage.getAllGenres();
    }

    public String findGenreById(int genreId) {
        String genre = filmStorage.findGenreById(genreId);
        return genre;
    }

    public List<String> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public String findMpaById(int ratingId) {
        String mpa = filmStorage.findMpaById(ratingId);
        return mpa;
    }
}
