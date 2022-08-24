package filmorate.ru.yandex.practicum.storage.dao;

import org.springframework.stereotype.Component;

@Component
public interface LikesDao {
    public void addLike(long filmId, long userId);
    public void deleteLike(long filmId, long userId);

}
