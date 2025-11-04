package com.tcc.api_ticket_sales.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.service.ticket_type.TicketTypeService;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionHandler;
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

import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeCreateResponseDTODefault;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeUpdateRequestDTODefault;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeUpdateRequestDTOInvalid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketTypeControllerTest {
    @Mock
    private TicketTypeService ticketTypeService;

    @InjectMocks
    private TicketTypeController ticketTypeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketTypeController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Test
    @Tag("unit")
    void update_shouldReturnStatusBadRequest_whenDateInvalid() throws Exception {
        UUID uuid = UUID.randomUUID();
        String invalidJson = """
            {
                "dateInitial": "2025-10-10 20:00",
                "dateFinal": "2025-10-10"
            }
        """;
        mockMvc.perform(
                patch(String.format("/ticket-type/%s", uuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void update_shouldReturnStatusUnprocessableEntity_whenDataInvalid() throws Exception {
        UUID uuid = UUID.randomUUID();
        TicketTypeUpdateRequestDTO request = createTicketTypeUpdateRequestDTOInvalid();

        mockMvc.perform(
                patch(String.format("/ticket-type/%s", uuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void update_shouldReturnStatusOk_whenTicketTypeIsUpdated () throws Exception {
        UUID uuid = UUID.randomUUID();
        TicketTypeUpdateRequestDTO dtoRequest = createTicketTypeUpdateRequestDTODefault();
        TicketTypeResponseDTO dtoResponse = createTicketTypeCreateResponseDTODefault();

        when(ticketTypeService.update(any(), any())).thenReturn(dtoResponse);

        mockMvc.perform(
                        patch(String.format("/ticket-type/%s", uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoRequest))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(dtoResponse.getId())))
                .andExpect(jsonPath("$.name").value(dtoResponse.getName()));
    }
}