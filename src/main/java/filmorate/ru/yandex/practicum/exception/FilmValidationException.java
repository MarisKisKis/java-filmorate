package filmorate.ru.yandex.practicum.exception;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException (String message) {
        super(message);
    }
}
