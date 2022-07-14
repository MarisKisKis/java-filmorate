package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.FilmNotFoundException;
import filmorate.ru.yandex.practicum.exception.UserNotFoundException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.model.User;
import filmorate.ru.yandex.practicum.storage.FilmStorage;
import filmorate.ru.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class FilmService {
    @Autowired
    UserStorage userStorage;
    @Autowired
    FilmStorage filmStorage;

    public void addLike(int userId, int filmId) throws UserNotFoundException, FilmNotFoundException {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.addLike(film, user);
    }

    public void deleteLike(int userId, int filmId) throws UserNotFoundException, FilmNotFoundException {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.deleteLike(film, user);
    }

    public ArrayList<Film> getTop10Films(){
        List<Film> films = (List<Film>) filmStorage.findAll();
        films.sort(Comparator.comparingInt((Film f) -> f.getLikes().size()));
        ArrayList<Film> top10Films = new ArrayList<>(10);
        for (Film film : films) {
            top10Films.add(film);
        }
        return top10Films;
    }
}
