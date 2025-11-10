package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


class HolderEntityTest {

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenNameIsNull(){
        LocalDate birthDate = LocalDate.now();

        Exception exception = assertThrows(BusinessException.class, () -> {
            HolderEntity.of(
                    null,
                    "teste@gmail.com",
                    birthDate
            );
        });
        assertEquals("Nome do titular inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenNameIsEmpty(){
        LocalDate birthDate = LocalDate.now();

        Exception exception = assertThrows(BusinessException.class, () -> {
            HolderEntity.of(
                    "",
                    "teste@gmail.com",
                    birthDate
            );
        });
        assertEquals("Nome do titular inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenEmailIsNull(){
        LocalDate birthDate = LocalDate.now();

        Exception exception = assertThrows(BusinessException.class, () -> {
            HolderEntity.of(
                    "teste",
                    null,
                    birthDate
            );
        });
        assertEquals("Email do titular inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenEmailIsEmpty(){
        LocalDate birthDate = LocalDate.now();

        Exception exception = assertThrows(BusinessException.class, () -> {
            HolderEntity.of(
                    "teste",
                    "",
                    birthDate
            );
        });
        assertEquals("Email do titular inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenBirthDateIsNull(){

        Exception exception = assertThrows(BusinessException.class, () -> {
            HolderEntity.of(
                    "teste",
                    "teste@gmail.com",
                    null
            );
        });
        assertEquals("Data de nascimento do titular inválida", exception.getMessage());
    }

}