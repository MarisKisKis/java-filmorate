package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.dao.FilmStorage;
import filmorate.ru.yandex.practicum.storage.dao.LikesDao;
import filmorate.ru.yandex.practicum.storage.dao.UserStorage;
import filmorate.ru.yandex.practicum.storage.dao.MpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikesDao likesDao;

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage, LikesDao likesDao) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likesDao = likesDao;
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
        likesDao.addLike(userId, filmId);
    }

    public void deleteLike(long userId, long filmId) throws NotFoundException {
        if (userId <= 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } if (filmId <= 0) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        likesDao.deleteLike(userId, filmId);
    }


    public List<Film> getTopFilms(int count) {
        List<Integer> top10FilmsIds = filmStorage.getTopFilms(count);
        List<Film> topCountFilms = new ArrayList<>();
        if (count< 10)   {
            for (int i = 0; i < count; i++) {
                topCountFilms.add(filmStorage.findFilmById(top10FilmsIds.get(i)));
            }
        } else {
            for (int i = 0; i < top10FilmsIds.size(); i++) {

                topCountFilms.add(filmStorage.findFilmById(top10FilmsIds.get(i)));
            }
        }
        System.out.println(topCountFilms.size());
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
}
