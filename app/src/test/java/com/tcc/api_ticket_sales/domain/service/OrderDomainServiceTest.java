package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderDomainServiceTest {
    OrderDomainService orderDomainService;

    @BeforeEach
    void setUp() {
        orderDomainService = new OrderDomainService();
    }

    @Test
    void createOrder_shouldReturnOrderEntity_whenTotalPriceIsValid(){
        OrderEntity orderEntity = orderDomainService.createOrder(BigDecimal.valueOf(30L));
        assertEquals(
                orderEntity.getTotalPrice(),
                BigDecimal.valueOf(30L)
        );
    }
}