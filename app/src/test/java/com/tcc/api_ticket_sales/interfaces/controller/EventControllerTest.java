package com.tcc.api_ticket_sales.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tcc.api_ticket_sales.application.service.event.EventService;
import com.tcc.api_ticket_sales.application.service.ticket_type.TicketTypeService;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionHandler;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.EventFactory.*;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    @Mock
    private EventService eventService;

    @Mock
    private TicketTypeService ticketTypeService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Test
    @Tag("unit")
    void createEvent_shouldReturnStatusCreated_whenEventIsCreated () throws Exception {
        EventCreateRequestDTO eventCreateRequestDTO = createEventCreateDTOValid();
        EventResponseDTO eventResponseDTO = createEventResponseDTO();

        when(eventService.create(any())).thenReturn(eventResponseDTO);

        mockMvc.perform(
                post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateRequestDTO))
        ).andExpect(status().isCreated())
        .andExpect(
                header().string("Location", "/event/"+ eventResponseDTO.getId())
        )
        .andExpect(jsonPath("$.id").value(String.valueOf(eventResponseDTO.getId())))
        .andExpect(jsonPath("$.name").value(eventResponseDTO.getName()));
    }

    @Test
    @Tag("unit")
    void createEvent_shouldReturnStatusBadRequest_whenDateInvalid() throws Exception {
        String invalidJson = """
            {
                "dateInitial": "2025-10-10 20:00",
                "dateFinal": "2025-10-10"
            }
        """;
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void createEvent_shouldReturnStatusUnprocessableEntity_whenDataInvalid() throws Exception {
        EventCreateRequestDTO eventCreateRequestDTO = createEventCreateDTOInvalid();
        mockMvc.perform(
                post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateRequestDTO))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void createTicketType_shouldReturnStatusBadRequest_whenDateInvalid() throws Exception {
        UUID uuid = UUID.randomUUID();
        String invalidJson = """
            {
                "dateInitial": "2025-10-10 20:00",
                "dateFinal": "2025-10-10"
            }
        """;
        mockMvc.perform(
                post(String.format("/event/%s/ticket-type", uuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void createTicketType_shouldReturnStatusUnprocessableEntity_whenDataInvalid() throws Exception {
        UUID uuid = UUID.randomUUID();
        TicketTypeCreateRequestDTO ticketCreateDTO = createTicketTypeCreateRequestDTOInvalid();

        mockMvc.perform(
                post(String.format("/event/%s/ticket-type", uuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateDTO))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void createTicketType_shouldReturnStatusCreated_whenTicketTypeIsCreated () throws Exception {
        UUID uuid = UUID.randomUUID();
        TicketTypeCreateRequestDTO dtoRequest = createTicketTypeCreateRequestDTOValid();
        TicketTypeResponseDTO dtoResponse = createTicketTypeCreateResponseDTODefault();

        when(ticketTypeService.create(any(), any())).thenReturn(dtoResponse);

        mockMvc.perform(
                        post(String.format("/event/%s/ticket-type", uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoRequest))
                ).andExpect(status().isCreated())
                .andExpect(
                        header().string("Location", "/ticket-type/"+ dtoResponse.getId())
                )
                .andExpect(jsonPath("$.id").value(String.valueOf(dtoResponse.getId())))
                .andExpect(jsonPath("$.name").value(dtoResponse.getName()));
    }
}