package com.tcc.api_ticket_sales.domain.exception;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessagesDomainTest {
    @Test
    @Tag("unit")
    void testConstants() {
        assertEquals("Data inicial maior que data final.", ErrorMessagesDomain.DATE_INITIAL_GREATER_THAN_DATE_FINAL);
        assertEquals("Data inválida.", ErrorMessagesDomain.DATE_INVALID);
        assertEquals("A data informada é anterior a data atual.", ErrorMessagesDomain.DATE_IS_BEFORE_TODAY);
    }

    @Test
    @Tag("unit")
    void constructorShouldBePrivateAndThrowException() throws Exception {
        Constructor<ErrorMessagesDomain> constructor = ErrorMessagesDomain.class.getDeclaredConstructor();

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