package com.tcc.api_ticket_sales.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tcc.api_ticket_sales.application.service.event.EventService;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionHandler;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.tcc.api_ticket_sales.factory.EventFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    @Mock
    private EventService eventService;

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
    public void createEvent_shouldReturnStatusCreated_whenEventIsCreated () throws Exception {
        EventCreateDTO eventCreateDTO = createEventCreateDTOValid();
        EventResponseDTO eventResponseDTO = createEventResponseDTO();

        when(eventService.createEvent(any())).thenReturn(eventResponseDTO);

        mockMvc.perform(
                post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateDTO))
        ).andExpect(status().isCreated())
        .andExpect(
                header().string("Location", "/event/"+ eventResponseDTO.getId())
        )
        .andExpect(jsonPath("$.id").value(String.valueOf(eventResponseDTO.getId())))
        .andExpect(jsonPath("$.name").value(eventResponseDTO.getName()));
    }

    @Test
    public void createEvent_shouldReturnStatusBadRequest_whenDateInvalid() throws Exception {
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
    public void createEvent_shouldReturnStatusUnprocessableEntity_whenDataInvalid() throws Exception {
        EventCreateDTO eventCreateDTO = createEventCreateDTOInvalid();
        System.out.println(eventCreateDTO.toString());
        mockMvc.perform(
                post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateDTO))
        ).andExpect(status().isUnprocessableEntity());
    }
}