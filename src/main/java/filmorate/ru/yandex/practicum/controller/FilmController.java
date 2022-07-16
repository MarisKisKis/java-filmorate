package filmorate.ru.yandex.practicum.controller;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.exception.ValidationException;
import filmorate.ru.yandex.practicum.model.Film;
import filmorate.ru.yandex.practicum.service.FilmService;
import filmorate.ru.yandex.practicum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    @Autowired
    UserService userService;
    @Autowired
    FilmService filmService;

    @GetMapping
    public Collection <Film> findAll() {
        log.trace("Текущее количество фильмов : {}", filmService.findAllFilms().size());
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws NotFoundException {
        validate(film);
        filmService.save(film);
        log.trace("Фильм " + film.getName() + " добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws NotFoundException {
        validate(film);
        filmService.updateFilm(film);
        log.trace("Фильм " + film.getName() + "обновлен");
        return film;
    }
    @GetMapping("/{filmId}")
    Film getFilm(@PathVariable long filmId) throws NotFoundException {
        log.info("Получили фильм по id ", filmId);
        return filmService.getFilm(filmId);
    }
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) throws NotFoundException {
        filmService.addLike(userId, filmId);
    }
    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long userId, @PathVariable long filmId) throws NotFoundException {
        filmService.deleteLike(userId, filmId);
    }

    @GetMapping("/films/popular")
    Collection<Film> getTopPopular(@RequestParam(defaultValue = "10") long count) throws NotFoundException {
        log.info("Получили " + count + " самых популярных фильмов");
        return filmService.getTopFilms(count);
    }
    public void validate(Film film) throws NotFoundException {
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
            throw new NotFoundException("Нет такого фильма");
        }
    }
}
