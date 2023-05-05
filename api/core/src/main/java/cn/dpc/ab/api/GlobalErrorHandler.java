package cn.dpc.ab.api;

import cn.dpc.ab.domain.exception.RecordNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException exception) {

        String message = exception.getMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(DuplicateKeyException exception) {

        String message = exception.getMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }


    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleWebExchangeBindException(WebExchangeBindException exception) {
        String message = exception.getAllErrors().get(0).getDefaultMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleWebExchangeBindException(RecordNotFoundException exception) {
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleIllegalArgumentException(RuntimeException exception) {

        String message = exception.getMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }

    @Data
    @AllArgsConstructor
    public static class ExceptionResponse {
        private String message;
    }
}
