package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    @Test
    void validateName() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setName(" ");
        assertThrows(FilmValidationException.class, () -> filmController.validateFilm(film) );
    }
    @Test
    void validateDescription() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setDescription("Военная драма о советском разведчике, внедрённом в высшие эшелоны власти нацистской Германии, снималась с 1971 по 1973 год. Показ фильма должен был начаться ко Дню Победы в мае 1973 года, но был отложен по политическим соображениям, из-за визита в эти дни советского лидера Леонида Брежнева в ФРГ. Первую серию картины зрители увидели 11 августа 1973 года. Фильм приобрёл широкую популярность в Советском Союзе уже во время премьерного показа, в связи с чем повторный показ состоялся уже через три месяца.");
        assertThrows(FilmValidationException.class, () -> filmController.validateFilm(film) );
    }
    @Test
    void validateDescription() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setDescription("Военная драма о советском разведчике, внедрённом в высшие эшелоны власти нацистской Германии, снималась с 1971 по 1973 год. Показ фильма должен был начаться ко Дню Победы в мае 1973 года, но был отложен по политическим соображениям, из-за визита в эти дни советского лидера Леонида Брежнева в ФРГ. Первую серию картины зрители увидели 11 августа 1973 года. Фильм приобрёл широкую популярность в Советском Союзе уже во время премьерного показа, в связи с чем повторный показ состоялся уже через три месяца.");
        assertThrows(FilmValidationException.class, () -> filmController.validateFilm(film) );
    }
}
