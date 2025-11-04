package com.tcc.api_ticket_sales.application.service.ticket_type;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.exception.*;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import com.tcc.api_ticket_sales.application.mapper.ticket_type.TicketTypeMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeCreateRequestDTOValid;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeCreateResponseDTODefault;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeUpdateRequestDTODefault;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketTypeServiceImplTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private TicketTypeMapper ticketTypeMapper;
    @Mock
    private TicketTypeDomainService ticketTypeDomainService;

    @InjectMocks
    private TicketTypeServiceImpl ticketServiceImpl;

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenEventNotFound() {
        // arrange
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();

        // action e assert
        assertThrows(
                EntityNotFoundException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }
    @Test
    @Tag("unit")
    void create_shouldReturnTicketTypeResponseDTO_whenSuccessful() {
        EventEntity event = createEventEntityWithId();
        UUID eventId = event.getId();
        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        TicketTypeResponseDTO ticketTypeResponseDTO = createTicketTypeCreateResponseDTODefault();
        TicketTypeCreateRequestDTO ticketTypeCreateRequestDTO = createTicketTypeCreateRequestDTOValid();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(new ArrayList<>());
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(new ArrayList<>());
        when(ticketTypeMapper.fromTicketTypeCreateRequestDTOToTicketTypeEntity(any(), any())).thenReturn(ticketType);
        when(ticketTypeDomainService.createTicketType(any(), any(), any())).thenReturn(ticketType);
        when(ticketTypeMapper.fromTicketTypeEntityToTicketTypeResponseDTO(any())).thenReturn(ticketTypeResponseDTO);

        TicketTypeResponseDTO result = ticketServiceImpl.create(eventId, ticketTypeCreateRequestDTO);

        assertNotNull(result);
        assertEquals(ticketTypeResponseDTO.getName(), result.getName());
    }


    @Test
    @Tag("unit")
    void create_shouldThrowTicketTypeAlreadyExistsException_whenTicketTypeNameExists() {
        EventEntity event = createEventEntityWithId();
        UUID eventId = event.getId();
        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        TicketTypeCreateRequestDTO dto = createTicketTypeCreateRequestDTOValid();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any()))
                .thenReturn(List.of(ticketType));

        assertThrows(TicketTypeAlreadyExistsException.class, () -> {
            ticketServiceImpl.create(eventId, dto);
        });
    }

    @Test
    @Tag("unit")
    void update_shouldThrowTicketTypeNotFoundException_whenTicketTypeNotFound() {
        // arrange
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.empty());
        TicketTypeUpdateRequestDTO dto = createTicketTypeUpdateRequestDTODefault();

        // action e assert
        assertThrows(
                TicketTypeNotFoundException.class,
                () -> ticketServiceImpl.update(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void update_shouldReturnTicketTypeResponseDTO_whenSuccessful() {
        TicketTypeEntity ticketType = createTicketTypeEntityWithId();
        TicketTypeResponseDTO ticketTypeResponseDTO = createTicketTypeCreateResponseDTODefault();
        TicketTypeUpdateRequestDTO dto = createTicketTypeUpdateRequestDTODefault();

        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketType));
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(new ArrayList<>());
        when(ticketTypeMapper.fromTicketTypeUpdateRequestDTOToTicketTypeEntity(any(), any())).thenReturn(ticketType);
        when(ticketTypeDomainService.updateTicketType(any())).thenReturn(ticketType);
        when(ticketTypeMapper.fromTicketTypeEntityToTicketTypeResponseDTO(any())).thenReturn(ticketTypeResponseDTO);

        TicketTypeResponseDTO result = ticketServiceImpl.update(UUID.randomUUID(), dto);

        assertNotNull(result);
        assertEquals(ticketTypeResponseDTO.getId(), result.getId());
    }


    @Test
    @Tag("unit")
    void update_shouldThrowTicketTypeAlreadyExistsException_whenTicketTypeNameExists() {
        TicketTypeEntity ticketType = createTicketTypeEntityWithId();
        TicketTypeUpdateRequestDTO dto = createTicketTypeUpdateRequestDTODefault();

        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketType));
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any()))
                .thenReturn(List.of(ticketType));

        assertThrows(TicketTypeAlreadyExistsException.class, () -> {
            ticketServiceImpl.update(UUID.randomUUID(), dto);
        });
    }
}