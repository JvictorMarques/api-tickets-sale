package com.tcc.api_ticket_sales.application.mapper.order;

import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDTO fromOrderEntityToOrderResponseDTO(OrderEntity orderEntity) {
        List<String> ticketsId = orderEntity.getTicketEntities() != null
                ? orderEntity.getTicketEntities().stream().map(ticketEntity -> ticketEntity.getId().toString()).toList()
                : List.of();

        return OrderResponseDTO.builder()
                .ticketsIds(ticketsId)
                .totalPrice(orderEntity.getTotalPrice())
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .build();
    }
}
