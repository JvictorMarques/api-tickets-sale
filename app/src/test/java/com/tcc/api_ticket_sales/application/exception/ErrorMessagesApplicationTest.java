package com.tcc.api_ticket_sales.application.exception;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessagesApplicationTest {

    @Test
    @Tag("unit")
    void testConstants() {
        assertEquals("Ingresso inválido.", ErrorMessagesApplication.TICKET_NOT_FOUND);
        assertEquals("Titular inválido.", ErrorMessagesApplication.HOLDER_NOT_FOUND);
        assertEquals("Evento encerrado.", ErrorMessagesApplication.EVENT_CLOSED);
        assertEquals("Evento indisponível.", ErrorMessagesApplication.EVENT_UNAVAILABLE);
        assertEquals("Evento existente.", ErrorMessagesApplication.EVENT_ALREADY_EXISTS);
    }

    @Test
    @Tag("unit")
    void constructorShouldBePrivateAndThrowException() throws Exception {
        Constructor<ErrorMessagesApplication> constructor = ErrorMessagesApplication.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()),
                "O construtor deve ser privado");

        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable cause = thrown.getCause();
        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }
}