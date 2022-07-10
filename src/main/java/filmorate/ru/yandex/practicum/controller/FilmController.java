package filmorate.ru.yandex.practicum.controller;

import filmorate.ru.yandex.practicum.exception.FilmValidationException;
import filmorate.ru.yandex.practicum.exception.UserValidationException;
import filmorate.ru.yandex.practicum.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> findAllfilms() {
        log.trace("Текущее количество фильмов : {}", films.size());
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws FilmValidationException {
        validateFilm(film);
        films.add(film);
        log.trace("Фильм добавлен, количество фильмов: ", films.size());
        return film;
    }

    public void validate(Film film) {
        if (film.getName().isEmpty()) {
            log.trace("ошибка названия!");
            throw new FilmValidationException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            log.trace("ошибка descriptiom");
            throw new FilmValidationException("Максимальная длина описания — 200 символов!");
        } else if ((film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))) {
            log.trace("ошибка даты");
            throw new FilmValidationException("Фильм не может быть раньше 28.12.1895");
        } else if (film.getDuration() <= 0) {
            log.trace("ошибка длительности");
            throw new FilmValidationException("Длительность должна быть положительной");
        } else if (film.getId() < 0) {
            log.trace("ошибка filmId");
            throw new UserValidationException("Id не может быть меньше 0");
        } else if (film.getId() == 0) {
            film.setId(1);
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws FilmValidationException {
        validateFilm(film);
        films.add(film);
        log.trace("Фильм обновлен");
        films.remove(0);
        return film;
    }

}
