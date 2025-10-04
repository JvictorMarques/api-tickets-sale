package com.tcc.api_ticket_sales.interfaces.controller.exception;

import com.tcc.api_ticket_sales.application.exception.DateFinalIsBeforeTodayException;
import com.tcc.api_ticket_sales.application.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.application.exception.DateInvalidException;
import com.tcc.api_ticket_sales.application.exception.EventUnavailableException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestExceptionMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<String> errors = fieldErrors.stream().map((fieldError) -> {
            return fieldError.getField() + ":" + fieldError.getDefaultMessage();
        }).toList();

        return new RestExceptionMessage("Erro de validação",
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public RestExceptionMessage handleEntityNotFoundException(EntityNotFoundException e){
        return new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                List.of());
    }

    @ExceptionHandler(EventUnavailableException.class)
    public RestExceptionMessage handleEventUnavailableException(EventUnavailableException e){
        return new RestExceptionMessage(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                List.of());
    }

    @ExceptionHandler(Exception.class)
    public RestExceptionMessage handleException(Exception e){
        return new RestExceptionMessage(e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                List.of()
        );
    }

    @ExceptionHandler(DateInvalidException.class)
    public RestExceptionMessage handleDateInvalidException(Exception e){
        return new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );
    }

    @ExceptionHandler(DateInitialGreaterThanDateFinalException.class)
    public RestExceptionMessage handleDateInitialGreaterThanDateFinalException(Exception e){
        return new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );
    }

    @ExceptionHandler(DateFinalIsBeforeTodayException.class)
    public RestExceptionMessage handleDateFinalIsBeforeTodayException(Exception e){
        return new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );
    }
}
