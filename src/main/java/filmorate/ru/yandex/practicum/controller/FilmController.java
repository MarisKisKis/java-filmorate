package filmorate.ru.yandex.practicum.controller;

import filmorate.ru.yandex.practicum.exception.FilmValidationException;
import filmorate.ru.yandex.practicum.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final HashMap <Long, Film> films = new HashMap<>();
    private static Integer generatorId = 0;

    @GetMapping
    public Collection <Film> findAll() {
        log.trace("Текущее количество фильмов : {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validate(film);
        long id = ++generatorId;
        film.setId(id);
        films.put(id, film);
        log.trace("Фильм " + film.getName() + " добавлен");
        return film;
    }

    public void validate(Film film) {
        if (film.getName().isEmpty()) {
            log.warn("Ошибка названия фильма " + film.getId()); //нецелесообразно вызывать в лог film.getName(),
            // так как по условию имя пустое
            throw new FilmValidationException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка descriptiom фильма " + film.getName());
            throw new FilmValidationException("Максимальная длина описания — 200 символов!");
        } else if ((film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))) {
            log.warn("Ошибка даты фильма " + film.getName());
            throw new FilmValidationException("Фильм не может быть раньше 28.12.1895");
        } else if (film.getDuration() <= 0) {
            log.warn("Ошибка длительности фильма " + film.getName());
            throw new FilmValidationException("Длительность должна быть положительной");
        } else if (film.getId() < 0) {
            log.warn("Ошибка Id фильма " + film.getName());
            throw new FilmValidationException("Id не может быть меньше 0");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.trace("Фильм " + film.getName() + "обновлен");
        return film;
    }
}
