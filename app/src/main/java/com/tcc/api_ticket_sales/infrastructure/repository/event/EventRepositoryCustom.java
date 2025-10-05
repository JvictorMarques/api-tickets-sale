package com.tcc.api_ticket_sales.infrastructure.repository.event;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;

public interface EventRepositoryCustom {

    boolean checkExists(EventCreateDTO event);
}
