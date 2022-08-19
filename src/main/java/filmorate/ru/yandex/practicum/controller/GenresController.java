package filmorate.ru.yandex.practicum.controller;

import filmorate.ru.yandex.practicum.model.Genre;
import filmorate.ru.yandex.practicum.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenresController {
    private final FilmService filmService;

    @Autowired
    public GenresController(FilmService filmService) {
        this.filmService = filmService;
    }

    @ResponseBody
    @GetMapping
    List<Genre> getAllGenres() {
        log.info("Получили все жанры");
        return filmService.getAllGenres();
    }

    @ResponseBody
    @GetMapping("/{id}")
    Genre getGenreById(@PathVariable int id) {
        log.info("Получили жанр под id {}", id);
        return filmService.findGenreById(id);
    }
}
