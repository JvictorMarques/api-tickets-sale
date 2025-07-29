package com.tcc.api_ticket_sales.application.service;

import com.tcc.api_ticket_sales.web.dto.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.web.dto.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.web.dto.EventResponseDTO;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;

import java.util.List;

public interface EventService {

    List<EventResponseDTO> getEventsAvailableByFilter(FilterEventRequestDTO filter);

    BuyTicketResponseDTO buyTicket(BuyTicketRequestDTO dataRequest);
}
