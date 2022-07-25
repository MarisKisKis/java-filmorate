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

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService (UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

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

    public List<Film> getTopFilms(int count) {
        Collection<Film> films = filmStorage.findAll();
        List<Film> topFilms1 = new ArrayList<>();
        for (Film film1 : films) {
            topFilms1.add(film1); // переносим объекты коллекции в список для применения сортировки
        }
        Collections.sort(topFilms1, new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                int result = Long.compare(o1.getLikes().size(), o2.getLikes().size()); // отсортировали
                return result;
            }
        });
        Collections.reverse(topFilms1);
        List<Film> topFilms = new ArrayList<>();
        if ((topFilms1.size() < 10) && (count == 10)) { // проверили, если count не задан и фильмов меньше 10
            count = topFilms1.size();
        } else {
            count = count; // вернули значение count, если он задан
        }
        topFilms = topFilms1.subList(0, count);
        return topFilms;
    }

    public Collection<Film> findAllFilms() {
        Collection<Film> allFilms;
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
