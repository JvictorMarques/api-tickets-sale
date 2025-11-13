package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.order.OrderService;
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

import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderResponseDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }


    @Test
    @Tag("unit")
    void getById_shouldReturnStatusBadRequest_whenParamUrlInvalid() throws Exception {
        mockMvc.perform(
                get(String.format("/order/%s", "teste123"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Tag("unit")
    void getById_shouldReturnOrderResponseDTO_whenOrderExists () throws Exception {
        when(orderService.getById(any())).thenReturn(createOrderResponseDTO());

        mockMvc.perform(
                get(String.format("/order/%s", UUID.randomUUID().toString()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}