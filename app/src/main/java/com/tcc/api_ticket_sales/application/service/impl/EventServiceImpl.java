package com.tcc.api_ticket_sales.application.service.impl;

import com.tcc.api_ticket_sales.application.exception.ErrorMessages;
import com.tcc.api_ticket_sales.application.exception.EventUnavailableException;
import com.tcc.api_ticket_sales.application.helpers.CheckDate;
import com.tcc.api_ticket_sales.application.service.EventService;
import com.tcc.api_ticket_sales.entity.EventEntity;
import com.tcc.api_ticket_sales.entity.HolderEntity;
import com.tcc.api_ticket_sales.entity.TicketEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.application.model.EventModel;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.web.dto.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.web.dto.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.web.dto.EventResponseDTO;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;
import com.tcc.api_ticket_sales.web.mapper.EventMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final HolderRepository holderRepository;

    @Override
    public List<EventResponseDTO> getEventsAvailableByFilter(FilterEventRequestDTO filter){
        CheckDate.checkIntervalDateInvalid(filter.dateInitial(), filter.dateFinal(), true);
        CheckDate.checkDateInitialGreaterThanDateFinal(filter.dateInitial(), filter.dateFinal());
        CheckDate.checkDateFinalIsBeforeToday(filter.dateFinal());

        List<EventModel> events = eventRepository.getEventsByFilter(filter);

        return events.stream()
                .map((event) -> EventMapper.fromEventModelToEventModelResponseDTO(event))
                .filter((event) ->  event.numberTicketsAvailable() > 0)
                .toList();
    }

    @Transactional
    @Override
    public BuyTicketResponseDTO buyTicket(BuyTicketRequestDTO dataRequest){
        TicketEntity ticket = ticketRepository.findById(dataRequest.ticketId()).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.TICKET_NOT_FOUND)
        );
        HolderEntity holder = holderRepository.findById(dataRequest.holderId()).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.HOLDER_NOT_FOUND)
        );
        EventEntity event = ticket.getEventEntity();

        if(event == null){
            throw new EntityNotFoundException(ErrorMessages.EVENT_NOT_FOUND);
        }

        Long numberTicketsAvailable = eventRepository.getTicketsAvailableByEventId(event.getId());

        if(numberTicketsAvailable <= 0){
            throw new EventUnavailableException();
        }

        List<HolderEntity> holdersTicket = ticket.getHolderEntities();
        holdersTicket.add(holder);
        ticket.setHolderEntities(holdersTicket);

        return new BuyTicketResponseDTO(event.getName(), event.getDateInitial(), event.getDateFinal());
    }
}
