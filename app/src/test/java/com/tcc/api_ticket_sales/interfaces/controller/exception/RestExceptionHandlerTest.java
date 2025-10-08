package com.tcc.api_ticket_sales.interfaces.controller.exception;

import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.application.exception.EventUnavailableException;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.domain.exception.DateInvalidException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.tcc.api_ticket_sales.application.exception.ErrorMessages.EVENT_ALREADY_EXISTS;
import static com.tcc.api_ticket_sales.application.exception.ErrorMessages.EVENT_UNAVAILABLE;
import static com.tcc.api_ticket_sales.domain.exception.ErrorMessages.DATE_INITIAL_GREATER_THAN_DATE_FINAL;
import static com.tcc.api_ticket_sales.domain.exception.ErrorMessages.DATE_INVALID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestExceptionHandlerTest {

    private final RestExceptionHandler restExceptionHandler = new RestExceptionHandler();

    @Test
    public void handleMethodArgumentNotValidException_shouldReturnRestExceptionMessage_whenMethodArgumentNotValid()
    {
        // Arrange
        FieldError fieldError1 = new FieldError("objectName", "name", "não pode ser vazio");
        FieldError fieldError2 = new FieldError("objectName", "email", "formato inválido");

        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleMethodArgumentNotValidException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro de validação", body.getMessage());
        assertTrue(body.getErrors().contains("name:não pode ser vazio"));
        assertTrue(body.getErrors().contains("email:formato inválido"));
        assertEquals(2, body.getErrors().size());
    }

    @Test
    void handleEntityNotFoundException_shouldReturnRestExceptionMessage_whenEntityNotFound() {
        // Arrange
        String errorMessage = "Entidade não encontrada";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleEntityNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(errorMessage), body.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenEventUnavailableException() {
        // Arrange
        EventUnavailableException exception = new EventUnavailableException();

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleEventUnavailableException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(EVENT_UNAVAILABLE, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(EVENT_UNAVAILABLE), body.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenException() {
        // Arrange
        String message = "Error de servirdor";
        Exception exception = new Exception(message);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(message, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(message), body.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenDateInvalidException() {
        // Arrange
        DateInvalidException exception = new DateInvalidException();

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleDateInvalidException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(DATE_INVALID, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(DATE_INVALID), body.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenDateInitialGreaterThanDateFinalException() {
        // Arrange
        DateInitialGreaterThanDateFinalException exception = new DateInitialGreaterThanDateFinalException();

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleDateInitialGreaterThanDateFinalException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(DATE_INITIAL_GREATER_THAN_DATE_FINAL, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(DATE_INITIAL_GREATER_THAN_DATE_FINAL), body.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenBusinessException() {
        // Arrange
        String message = "Erro na regra de negócio";
        BusinessException exception = new BusinessException(message);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleBusinessException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(message, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(message), body.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenEventAlreadyExistsException() {
        // Arrange
        EventAlreadyExistsException exception = new EventAlreadyExistsException();

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleEventAlreadyExistsException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode().value());

        RestExceptionMessage body =  response.getBody();
        assertNotNull(body);
        assertEquals(EVENT_ALREADY_EXISTS, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(EVENT_ALREADY_EXISTS), body.getErrors());
    }

    @Test
    void handleHttpMessageNotReadableException_shouldReturnDateFormatMessage_whenContainsLocalDateTime() {
        // Arrange
        String exceptionMessage = "Failed to deserialize java.time.LocalDateTime";
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(exceptionMessage);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleHttpMessageNotReadableException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals("Formato de data inválido. Use o padrão yyyy-MM-dd'T'HH:mm:ss", body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(exceptionMessage), body.getErrors());
    }

    @Test
    void handleHttpMessageNotReadableException_shouldReturnJsonFormatMessage_whenDoesNotContainLocalDateTime() {
        // Arrange
        String exceptionMessage = "Unexpected character ('}' (code 125)): was expecting field name";
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(exceptionMessage);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleHttpMessageNotReadableException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals("Formato de JSON inválido ou campo malformado", body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(exceptionMessage), body.getErrors());
    }
}