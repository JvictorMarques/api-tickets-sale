package com.tcc.api_ticket_sales.application.service.order;

import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;

import java.util.UUID;

public interface OrderService {
    OrderResponseDTO getById(UUID id);
}
