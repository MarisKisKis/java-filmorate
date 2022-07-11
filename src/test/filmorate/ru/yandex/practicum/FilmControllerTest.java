package filmorate.ru.yandex.practicum;

import filmorate.ru.yandex.practicum.controller.FilmController;
import filmorate.ru.yandex.practicum.model.Film;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    @Test
    void validateName() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setName(" ");
        assertThrows(RuntimeException.class, () -> filmController.validate(film) );
    }
    @Test
    void validateDescription() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setDescription("Военная драма о советском разведчике, внедрённом в высшие эшелоны власти нацистской Германии, снималась с 1971 по 1973 год. Показ фильма должен был начаться ко Дню Победы в мае 1973 года, но был отложен по политическим соображениям, из-за визита в эти дни советского лидера Леонида Брежнева в ФРГ. Первую серию картины зрители увидели 11 августа 1973 года. Фильм приобрёл широкую популярность в Советском Союзе уже во время премьерного показа, в связи с чем повторный показ состоялся уже через три месяца.");
        assertThrows(RuntimeException.class, () -> filmController.validate(film) );
    }
    @Test
    void validateRelease() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setReleaseDate(LocalDate.MAX);
        assertThrows(RuntimeException.class, () -> filmController.validate(film) );
    }
    @Test
    void validateDuration() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setDuration(-100);
        assertThrows(RuntimeException.class, () -> filmController.validate(film) );
    }
}
