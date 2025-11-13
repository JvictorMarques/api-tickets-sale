package com.tcc.api_ticket_sales.application.mapper.order;

import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createListTicketEntityPaymentApproved;
import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapper();

    @Tag("unit")
    @Test
    void fromOrderEntityToOrderResponseDTO_returnOrderResponseDTO_whenOrderEntity() {
        OrderEntity order = createOrderEntityWithId();
        List<TicketEntity> tickets = createListTicketEntityPaymentApproved();
        order.setTicketEntities(tickets);

        OrderResponseDTO orderResponseDTO = orderMapper.fromOrderEntityToOrderResponseDTO(order);

        assertNotNull(orderResponseDTO);
        assertEquals(order.getTicketEntities().size(), tickets.size());
        assertEquals(orderResponseDTO.getTotalPrice(), order.getTotalPrice());
        assertEquals(orderResponseDTO.getCreatedAt(), order.getCreatedAt());
        assertEquals(orderResponseDTO.getUpdatedAt(), order.getUpdatedAt());
    }
}