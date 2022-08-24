package filmorate.ru.yandex.practicum.service;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.model.Mpa;
import filmorate.ru.yandex.practicum.storage.dao.MpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    public Mpa findMpaById(int ratingId) {
        if (ratingId <= 0) {
            throw new NotFoundException("MPA с id " + ratingId + " не найден");
        }
        Mpa mpa = mpaDao.findMpaById(ratingId);
        return mpa;
    }

}
