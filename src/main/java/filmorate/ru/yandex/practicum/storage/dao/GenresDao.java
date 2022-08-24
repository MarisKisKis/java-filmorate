package filmorate.ru.yandex.practicum.storage.dao;

import filmorate.ru.yandex.practicum.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GenresDao {
    public List<Genre> getAllGenres();
    public Genre findGenreById(int genreId);
}
