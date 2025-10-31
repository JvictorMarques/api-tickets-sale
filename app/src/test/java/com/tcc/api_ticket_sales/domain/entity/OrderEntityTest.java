package com.tcc.api_ticket_sales.domain.entity;


import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenTotalPriceIsNull(){
        Exception exception = assertThrows(BusinessException.class, () -> {
            OrderEntity.of(null);
        });

        assertEquals("O valor total do pedido inválido.", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenTotalPriceIsBeforeZero(){
        Exception exception = assertThrows(BusinessException.class, () -> {
            OrderEntity.of(BigDecimal.valueOf(-15));
        });

        assertEquals("O valor total do pedido inválido.", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenTotalPriceIsZero(){
        Exception exception = assertThrows(BusinessException.class, () -> {
            OrderEntity.of(BigDecimal.valueOf(0));
        });

        assertEquals("O valor total do pedido inválido.", exception.getMessage());
    }

}