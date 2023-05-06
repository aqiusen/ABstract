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

/**
 * Global error handler for the REST API.
 */
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalErrorHandler {

    /**
     * handle record not found exception.
     *
     * @param exception IllegalArgumentException
     * @return ExceptionResponse
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException exception) {

        String message = exception.getMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }

    /**
     * handle duplicate key exception.
     *
     * @param exception DuplicateKeyException
     * @return ExceptionResponse
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDuplicateKeyException(DuplicateKeyException exception) {

        String message = exception.getMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }


    /**
     * handle web exchange bind exception.
     *
     * @param exception WebExchangeBindException
     * @return ExceptionResponse
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleWebExchangeBindException(WebExchangeBindException exception) {
        String message = exception.getAllErrors().get(0).getDefaultMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }

    /**
     * handle record not found exception.
     *
     * @param exception RecordNotFoundException
     * @return ExceptionResponse
     */
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleRecordNotFoundException(RecordNotFoundException exception) {
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(exception.getMessage());
    }

    /**
     * handle runtime exception.
     *
     * @param exception RuntimeException
     * @return ExceptionResponse
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleRuntimeException(RuntimeException exception) {

        String message = exception.getMessage();
        log.error("VALIDATION_ERROR", exception);
        return new ExceptionResponse(message);
    }

    /**
     * Exception response.
     */
    @Data
    @AllArgsConstructor
    public static class ExceptionResponse {
        private String message;
    }
}
