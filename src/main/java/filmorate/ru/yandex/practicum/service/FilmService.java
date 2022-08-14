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
    private long generatorId;

    @Autowired
    public FilmService (UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Optional<Film> findAllFilms() {
        Optional<Film> allFilms;
        allFilms = filmStorage.findAll();
        return allFilms;
    }

    public Optional<Film> getFilm(long filmId) throws NotFoundException {
        if (filmId < 0) {
            throw new NotFoundException("Не найден фильм с id" + filmId);
        }
        final Optional<Film> film = filmStorage.findFilmById(filmId);
        return film;
    }

    public void addLike(long userId, long filmId) throws NotFoundException {
        Optional<User> user = userStorage.findUserById(userId);
        Optional<Film> film = filmStorage.findFilmById(filmId);
        long likenessId = 0;
        likenessId = generatorId++;
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.addLike(film, user, likenessId);
    }

    public void deleteLike(long userId, long filmId) throws NotFoundException {
        Optional<User> user = userStorage.findUserById(userId);
        Optional<Film> film = filmStorage.findFilmById(filmId);
        long likenessId = filmStorage.findLikenessId(userId, filmId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.deleteLike(likenessId);
    }


    public List<Film> getTopFilms(int count) {
        Optional<Film> films = filmStorage.findAll();
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

     */

    public Optional<Film> save(Film film) {
        return filmStorage.saveFilm(film);
    }

    public void updateFilm(Film film) throws NotFoundException {
        if (film == null) {
            throw new NotFoundException("Фильм с id " + film + " не найден");
        }
        filmStorage.updateFilm(film);
    }
}
