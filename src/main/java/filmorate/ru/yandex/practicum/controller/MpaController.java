package filmorate.ru.yandex.practicum.controller;
import filmorate.ru.yandex.practicum.model.Mpa;
import filmorate.ru.yandex.practicum.service.MpaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @ResponseBody
    @GetMapping
    List<Mpa> getAllMpa() {
        log.info("Получили все рейтинги");
        return mpaService.getAllMpa();
    }

    @ResponseBody
    @GetMapping("/{mpa}")
    Mpa getMpaById(@PathVariable int mpa) {
        log.info("Получили mpa под id {}", mpa);
        return mpaService.findMpaById(mpa);
    }
}
