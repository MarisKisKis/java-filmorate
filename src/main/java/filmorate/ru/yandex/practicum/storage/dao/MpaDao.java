package filmorate.ru.yandex.practicum.storage.dao;

import filmorate.ru.yandex.practicum.model.Mpa;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MpaDao {
    public List<Mpa> getAllMpa();
    public Mpa findMpaById(int ratingId);
}
