package com.tcc.api_ticket_sales.application.service;

import com.tcc.api_ticket_sales.application.exception.DateFinalIsBeforeTodayException;
import com.tcc.api_ticket_sales.application.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.application.exception.DateInvalidException;
import com.tcc.api_ticket_sales.application.service.impl.EventServiceImpl;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.application.model.EventModel;
import com.tcc.api_ticket_sales.web.dto.EventResponseDTO;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    @Tag("unit")
    @DisplayName("Should return only events with available tickets")
    void getEventsAvailable(){
        FilterEventRequestDTO filter = new FilterEventRequestDTO(LocalDate.now(), LocalDate.now(), null, null);
        EventModel eventWithTickets = EventModel.builder()
                .id(UUID.randomUUID())
                .name("Event with tickets")
                .numberTicketsAvailable((long) 5)
                .numberTicketsUnavailable((long) 4)
                .build();
        EventModel eventWithoutTickets = EventModel.builder()
                .id(UUID.randomUUID())
                .name("Event with tickets")
                .numberTicketsAvailable((long) 0)
                .numberTicketsUnavailable((long) 4)
                .build();


        when(eventRepository.getEventsByFilter(filter)).thenReturn(List.of(eventWithTickets, eventWithoutTickets));

        List<EventResponseDTO> events = eventService.getEventsAvailableByFilter(filter);

        assertEquals(1, events.size());

    }

    @Test
    @Tag("unit")
    @DisplayName("Should return empty because there are no events available")
    void getEventsAvailableEmpty(){
        FilterEventRequestDTO filter = new FilterEventRequestDTO(LocalDate.now(), LocalDate.now(), null, null);
        EventModel eventWithoutTickets = EventModel.builder()
                .id(UUID.randomUUID())
                .name("Event with tickets")
                .numberTicketsAvailable((long) 0)
                .numberTicketsUnavailable((long) 4)
                .build();


        when(eventRepository.getEventsByFilter(filter)).thenReturn(List.of(eventWithoutTickets));

        List<EventResponseDTO> events = eventService.getEventsAvailableByFilter(filter);

        assertEquals(0, events.size());

    }

    @Test
    @Tag("unit")
    @DisplayName("Should return exeception DateInvalid because dates null")
    void getEventsAvailableDateNull(){
        FilterEventRequestDTO filter = new FilterEventRequestDTO(null, null, null, null);

        assertThrows(DateInvalidException.class, () -> eventService.getEventsAvailableByFilter(filter));
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return exeception DateInvalid because date final null")
    void getEventsAvailableDateFinalNull(){
        FilterEventRequestDTO filter = new FilterEventRequestDTO(LocalDate.now(), null, null, null);

        assertThrows(DateInvalidException.class, () -> eventService.getEventsAvailableByFilter(filter));
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return exeception DateInitialGreaterThanDateFinalException")
    void getEventsAvailableDateInitialGreaterThanDateFinal(){
        FilterEventRequestDTO filter = new FilterEventRequestDTO(
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                null,
                null
        );

        assertThrows(DateInitialGreaterThanDateFinalException.class, () -> eventService.getEventsAvailableByFilter(filter));
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return exeception DateFinalIsBeforeTodayException")
    void getEventsAvailableDateFinalIsBeforeToday(){
        FilterEventRequestDTO filter = new FilterEventRequestDTO(
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1),
                null,
                null
        );

        assertThrows(DateFinalIsBeforeTodayException.class, () -> eventService.getEventsAvailableByFilter(filter));
    }


}
