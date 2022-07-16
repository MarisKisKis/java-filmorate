package filmorate.ru.yandex.practicum.controller;

import filmorate.ru.yandex.practicum.exception.NotFoundException;
import filmorate.ru.yandex.practicum.exception.ServerErrorException;
import filmorate.ru.yandex.practicum.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CharSequence handleNotFoundException(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CharSequence handleValidationException(final ValidationException e) {
        log.info("400 {}", e.getMessage());
        return e.getMessage();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CharSequence handleServerErrorException(final ServerErrorException e) {
        log.info("500 {}", e.getMessage());
        return e.getMessage();
    }
}
