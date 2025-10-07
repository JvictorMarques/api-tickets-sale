package com.tcc.api_ticket_sales.interfaces.controller.exception;

import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.application.exception.EventUnavailableException;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.domain.exception.DateInvalidException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
        RestExceptionMessage response = restExceptionHandler.handleMethodArgumentNotValidException(exception);

        // Assert
        assertNotNull(response);
        assertEquals("Erro de validação", response.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertTrue(response.getErrors().contains("name:não pode ser vazio"));
        assertTrue(response.getErrors().contains("email:formato inválido"));
        assertEquals(2, response.getErrors().size());
    }

    @Test
    void handleEntityNotFoundException_shouldReturnRestExceptionMessage_whenEntityNotFound() {
        // Arrange
        String errorMessage = "Entidade não encontrada";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        // Act
        RestExceptionMessage response = restExceptionHandler.handleEntityNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(errorMessage), response.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenEventUnavailableException() {
        // Arrange
        EventUnavailableException exception = new EventUnavailableException();

        // Act
        RestExceptionMessage response = restExceptionHandler.handleEventUnavailableException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(EVENT_UNAVAILABLE, response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(EVENT_UNAVAILABLE), response.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenException() {
        // Arrange
        String message = "Error de servirdor";
        Exception exception = new Exception(message);

        // Act
        RestExceptionMessage response = restExceptionHandler.handleException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(message), response.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenDateInvalidException() {
        // Arrange
        DateInvalidException exception = new DateInvalidException();

        // Act
        RestExceptionMessage response = restExceptionHandler.handleDateInvalidException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(DATE_INVALID, response.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(DATE_INVALID), response.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenDateInitialGreaterThanDateFinalException() {
        // Arrange
        DateInitialGreaterThanDateFinalException exception = new DateInitialGreaterThanDateFinalException();

        // Act
        RestExceptionMessage response = restExceptionHandler.handleDateInitialGreaterThanDateFinalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(DATE_INITIAL_GREATER_THAN_DATE_FINAL, response.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(DATE_INITIAL_GREATER_THAN_DATE_FINAL), response.getErrors());; // lista vazia
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenBusinessException() {
        // Arrange
        String message = "Erro na regra de negócio";
        BusinessException exception = new BusinessException(message);

        // Act
        RestExceptionMessage response = restExceptionHandler.handleBusinessException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(message), response.getErrors());
    }

    @Test
    void handleEventUnavailableException_shouldReturnRestExceptionMessage_whenEventAlreadyExistsException() {
        // Arrange
        EventAlreadyExistsException exception = new EventAlreadyExistsException();

        // Act
        RestExceptionMessage response = restExceptionHandler.handleEventAlreadyExistsException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(EVENT_ALREADY_EXISTS, response.getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(EVENT_ALREADY_EXISTS), response.getErrors());
    }

    @Test
    void handleHttpMessageNotReadableException_shouldReturnDateFormatMessage_whenContainsLocalDateTime() {
        // Arrange
        String exceptionMessage = "Failed to deserialize java.time.LocalDateTime";
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(exceptionMessage);

        // Act
        RestExceptionMessage response = restExceptionHandler.handleHttpMessageNotReadableException(exception);

        // Assert
        assertNotNull(response);
        assertEquals("Formato de data inválido. Use o padrão yyyy-MM-dd'T'HH:mm:ss", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(exceptionMessage), response.getErrors());
    }

    @Test
    void handleHttpMessageNotReadableException_shouldReturnJsonFormatMessage_whenDoesNotContainLocalDateTime() {
        // Arrange
        String exceptionMessage = "Unexpected character ('}' (code 125)): was expecting field name";
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(exceptionMessage);

        // Act
        RestExceptionMessage response = restExceptionHandler.handleHttpMessageNotReadableException(exception);

        // Assert
        assertNotNull(response);
        assertEquals("Formato de JSON inválido ou campo malformado", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertNotNull(response.getTimeStamp());
        assertEquals(List.of(exceptionMessage), response.getErrors());
    }
}