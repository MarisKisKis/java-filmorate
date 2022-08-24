package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Genre;
import filmorate.ru.yandex.practicum.storage.dao.GenresDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenresDao genresDao;

    @Autowired
    public GenreService(GenresDao genresDao) {
        this.genresDao = genresDao;
    }

    public List<Genre> getAllGenres() {
        return genresDao.getAllGenres();
    }

    public Genre findGenreById(int genreId) {
        if (genreId <= 0) {
            throw new NotFoundException("Жанр с id " + genreId+ " не найден");
        }
        Genre genre = genresDao.findGenreById(genreId);
        return genre;
    }
}
