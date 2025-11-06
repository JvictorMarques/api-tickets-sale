package com.tcc.api_ticket_sales.infrastructure.repository.event;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {

    List<EventEntity> checkExists(String name, String location, LocalDateTime dateInitial, LocalDateTime dateFinal);
}
