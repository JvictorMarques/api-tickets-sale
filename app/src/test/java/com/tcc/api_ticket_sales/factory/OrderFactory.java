package com.tcc.api_ticket_sales.factory;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderFactory {
    public static OrderEntity createOrderEntityWithoutId(){
        return OrderEntity.of(
                BigDecimal.valueOf(30)
        );
    }

    public static OrderEntity createOrderEntityWithId(){
        OrderEntity order =  OrderEntity.of(
                BigDecimal.valueOf(30)
        );

        order.setId(UUID.randomUUID());

        return order;
    }
}
