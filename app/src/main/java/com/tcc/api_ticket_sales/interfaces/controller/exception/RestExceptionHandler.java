package com.tcc.api_ticket_sales.interfaces.controller.exception;

import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.application.exception.EventClosedException;
import com.tcc.api_ticket_sales.application.exception.TicketAlreadyExistsException;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.domain.exception.DateInvalidException;
import com.tcc.api_ticket_sales.application.exception.EventUnavailableException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestExceptionMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<String> errors = fieldErrors.stream().map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage()).toList();

        RestExceptionMessage body = new RestExceptionMessage("Erro de validação",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now(),
                errors);

        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestExceptionMessage> handleEntityNotFoundException(EntityNotFoundException e){
        RestExceptionMessage body = new RestExceptionMessage(e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                List.of(e.getMessage()));

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EventUnavailableException.class)
    public ResponseEntity<RestExceptionMessage> handleEventUnavailableException(EventUnavailableException e){
        RestExceptionMessage body = new RestExceptionMessage(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                List.of(e.getMessage()));

        return  ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestExceptionMessage> handleException(Exception e){
        RestExceptionMessage body = new RestExceptionMessage(e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler(DateInvalidException.class)
    public ResponseEntity<RestExceptionMessage> handleDateInvalidException(Exception e){
        RestExceptionMessage body = new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(DateInitialGreaterThanDateFinalException.class)
    public ResponseEntity<RestExceptionMessage> handleDateInitialGreaterThanDateFinalException(Exception e){
        RestExceptionMessage body = new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestExceptionMessage> handleBusinessException(Exception e){
        RestExceptionMessage body = new RestExceptionMessage(e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestExceptionMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        String message = e.getMessage().contains("LocalDateTime")
                ? "Formato de data inválido. Use o padrão yyyy-MM-dd'T'HH:mm:ss"
                : "Formato de JSON inválido ou campo malformado";

        RestExceptionMessage body = new RestExceptionMessage(message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity<RestExceptionMessage> handleEventAlreadyExistsException(EventAlreadyExistsException e){
        RestExceptionMessage body = new RestExceptionMessage(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(TicketAlreadyExistsException.class)
    public ResponseEntity<RestExceptionMessage> handleEventAlreadyExistsException(TicketAlreadyExistsException e){
        RestExceptionMessage body = new RestExceptionMessage(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT) .body(body);
    }

    @ExceptionHandler(EventClosedException.class)
    public ResponseEntity<RestExceptionMessage> handleEventNotFoundException(EventClosedException e){
        RestExceptionMessage body = new RestExceptionMessage(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                List.of(e.getMessage())
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT) .body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestExceptionMessage> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        String message = String.format("Parâmetro '%s' inválido: valor '%s' não pôde ser convertido para o tipo %s.",
                e.getName(),
                e.getValue(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "desconhecido");

        RestExceptionMessage body = new RestExceptionMessage(
                message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                List.of(message)
        );

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(body);
    }
}
