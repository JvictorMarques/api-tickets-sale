package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderDomainService {
    public OrderEntity createOrder(BigDecimal totalPrice){
        return OrderEntity.of(
                totalPrice
        );
    }
}
