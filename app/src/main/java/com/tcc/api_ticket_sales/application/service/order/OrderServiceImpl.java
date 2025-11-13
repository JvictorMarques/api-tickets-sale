package com.tcc.api_ticket_sales.application.service.order;

import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;
import com.tcc.api_ticket_sales.application.exception.OrderNotFoundException;
import com.tcc.api_ticket_sales.application.mapper.order.OrderMapper;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderResponseDTO getById(UUID id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id.toString()));

        if(order.getDeletedAt() != null) throw new OrderNotFoundException(id.toString());

        return orderMapper.fromOrderEntityToOrderResponseDTO(order);
    }
}
