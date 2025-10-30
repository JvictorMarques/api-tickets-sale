package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.exception.BuyTicketHolderDuplicateRequestException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeNotFoundException;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.tcc.api_ticket_sales.factory.BuyTicketFactory.createBuyTicketRequestDTO;
import static com.tcc.api_ticket_sales.factory.BuyTicketFactory.createBuyTicketWithHolderDuplicate;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BuyTicketValidatorTest {
    @Mock
    private TicketTypeDomainService ticketTypeDomainService;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @InjectMocks
    private BuyTicketValidator buyTicketValidator;

    @Tag("unit")
    @Test
    void validate_ShouldBuyTicketHolderDuplicateRequestException_WhenHolderDuplicate(){
        assertThrows(BuyTicketHolderDuplicateRequestException.class, () -> {
            buyTicketValidator.validate(createBuyTicketWithHolderDuplicate());
        });
    }

    @Tag("unit")
    @Test
    void validate_ShouldTicketTypeNotFoundException_WhenTicketTypeNotFound(){
        assertThrows(TicketTypeNotFoundException.class, () -> {
            buyTicketValidator.validate(createBuyTicketRequestDTO());
        });
    }

    @Tag("unit")
    @Test
    void validate_ShouldNotThrowException_WhenNoHolderDuplicateAndTicketTypeExists() {
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketTypeEntity));

        assertDoesNotThrow(() -> {
            buyTicketValidator.validate(createBuyTicketRequestDTO());
        });
    }
}