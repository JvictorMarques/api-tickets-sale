package com.tcc.api_ticket_sales.application.service.order;

import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;
import com.tcc.api_ticket_sales.application.exception.OrderNotFoundException;
import com.tcc.api_ticket_sales.application.mapper.order.OrderMapper;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderResponseDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Tag("unit")
    @Test
    void getById_shouldThrowOrderNotFound_whenIdNotExists(){
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderServiceImpl.getById(UUID.randomUUID()));
    }

    @Tag("unit")
    @Test
    void getById_shouldThrowOrderNotFound_whenOrderDeleted(){
        OrderEntity order = createOrderEntityWithId();
        order.setDeletedAt(LocalDateTime.now());

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThrows(OrderNotFoundException.class, () -> orderServiceImpl.getById(UUID.randomUUID()));
    }

    @Tag("unit")
    @Test
    void getById_shouldReturnOrderResponseDTO_whenOrderExists(){
        OrderEntity order = createOrderEntityWithId();
        OrderResponseDTO response = createOrderResponseDTO();

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderMapper.fromOrderEntityToOrderResponseDTO(any())).thenReturn(response);

        assertEquals(response, orderServiceImpl.getById(order.getId()));
    }
}