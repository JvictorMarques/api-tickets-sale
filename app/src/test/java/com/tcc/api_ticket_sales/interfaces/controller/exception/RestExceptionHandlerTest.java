package com.tcc.api_ticket_sales.interfaces.controller.exception;

import com.tcc.api_ticket_sales.domain.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestExceptionHandlerTest {

    private final RestExceptionHandler restExceptionHandler = new RestExceptionHandler();

    @Test
    @Tag("unit")
    void handleMethodArgumentNotValidException_shouldReturnRestExceptionMessage_whenMethodArgumentNotValid()
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
    @Tag("unit")
    void handleEntityNotFoundException_shouldReturnRestExceptionMessage_whenEntityNotFound() {
        // Arrange
        String errorMessage = "Entidade não encontrada";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleEntityNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(errorMessage), body.getErrors());
    }

    @Test
    @Tag("unit")
    void handleBadRequestException_shouldReturnRestExceptionMessage_whenBadRequestException() {
        // Arrange
        String messageTest = "Test Bad Request";
        BadRequestException exception = new BadRequestException(messageTest);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleBadRequestException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(messageTest, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(messageTest), body.getErrors());
    }

    @Test
    @Tag("unit")
    void handleException_shouldReturnRestExceptionMessage_whenException() {
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
    @Tag("unit")
    void handleConflictException_shouldReturnRestExceptionMessage_whenConflictException() {
        // Arrange
        String messageTest = "Test Conflict";
        ConflictException exception = new ConflictException(messageTest);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleConflictException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(messageTest, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(messageTest), body.getErrors());
    }

    @Test
    @Tag("unit")
    void handleBusinessException_shouldReturnRestExceptionMessage_whenBusinessException() {
        String messageTest = "Test Business";
        BusinessException exception = new BusinessException(messageTest);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleBusinessException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(messageTest, body.getMessage());
        assertNotNull(body.getTimeStamp());
        assertEquals(List.of(messageTest), body.getErrors());
    }

    @Test
    @Tag("unit")
    void handleHttpMessageNotReadableException_shouldReturnRestExceptionMessage_whenContainsLocalDateTime() {
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
    @Tag("unit")
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

    @Test
    @Tag("unit")
    void handleMethodArgumentTypeMismatchException_shouldReturnRestExceptionMessage_whenMethodArgumentTypeMismatchException() {
        // valor inválido
        Object value = "abc";

        // tipo esperado
        Class<?> requiredType = Long.class;

        // nome do parâmetro
        String paramName = "eventId";

        MethodParameter methodParam = Mockito.mock(MethodParameter.class);

        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(value, requiredType, paramName, methodParam, null);

        // Act
        ResponseEntity<RestExceptionMessage> response = restExceptionHandler.handleMethodArgumentTypeMismatchException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

        RestExceptionMessage body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains(paramName));
        assertNotNull(body.getTimeStamp());
    }
}