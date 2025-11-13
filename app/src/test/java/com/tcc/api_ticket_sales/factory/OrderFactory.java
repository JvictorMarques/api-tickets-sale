package com.tcc.api_ticket_sales.factory;
import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import org.instancio.Instancio;

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

    public static OrderResponseDTO createOrderResponseDTO(){
        return Instancio.create(OrderResponseDTO.class);
    }
}
