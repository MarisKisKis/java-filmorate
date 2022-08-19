package filmorate.ru.yandex.practicum.controller;
import filmorate.ru.yandex.practicum.model.Mpa;
import filmorate.ru.yandex.practicum.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @ResponseBody
    @GetMapping
    List<Mpa> getAllMpa() {
        log.info("Получили все рейтинги");
        return filmService.getAllMpa();
    }

    @ResponseBody
    @GetMapping("/{mpa}")
    Mpa getMpaById(@PathVariable int mpa) {
        log.info("Получили mpa под id {}", mpa);
        return filmService.findMpaById(mpa);
    }
}
