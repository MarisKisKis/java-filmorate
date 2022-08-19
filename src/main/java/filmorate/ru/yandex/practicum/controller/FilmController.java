package filmorate.ru.yandex.practicum.controller;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.exception.ValidationException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.service.FilmService;
import filmorate.ru.yandex.practicum.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final UserService userService;
    private final FilmService filmService;

    @Autowired
    public FilmController (UserService userService, FilmService filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.trace("Текущее количество фильмов : {}", filmService.findAllFilms());
        return filmService.findAllFilms();
    }

    @SneakyThrows
    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validate(film);
        filmService.save(film);
        log.trace("Фильм " + film.getName() + " добавлен");
        return film;
    }

    @SneakyThrows
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validate(film);
        filmService.updateFilm(film);
        log.trace("Фильм " + film.getName() + "обновлен");
        return film;
    }
    @SneakyThrows
    @GetMapping("/{filmId}")
    Film getFilm(@PathVariable long filmId) {
        log.info("Получили фильм по id ", filmId);
        return filmService.getFilm(filmId);
    }

    @SneakyThrows
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLike(userId, filmId);
    }
    @SneakyThrows
    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long userId, @PathVariable long filmId) {
        filmService.deleteLike(userId, filmId);
    }
    @ResponseBody
    @GetMapping("/popular")
    List<Film> getTopPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получили " + count + " самых популярных фильмов");
        return filmService.getTopFilms(count);
    }

    public void validate(Film film) {
        if (film.getName().isEmpty()) {
            log.warn("Ошибка названия фильма " + film.getId()); //нецелесообразно вызывать в лог film.getName(),
            // так как по условию имя пустое
            throw new ValidationException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка descriptiom фильма " + film.getName());
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        } else if ((film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))) {
            log.warn("Ошибка даты фильма " + film.getName());
            throw new ValidationException("Фильм не может быть раньше 28.12.1895");
        } else if (film.getDuration() <= 0) {
            log.warn("Ошибка длительности фильма " + film.getName());
            throw new ValidationException("Длительность должна быть положительной");
        } else if (film.getId() < 0) {
            log.warn("Ошибка Id фильма " + film.getName());
           // throw new ValidationException("Id не может быть меньше 0");
            try {
                throw new NotFoundException("Нет такого фильма");
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
