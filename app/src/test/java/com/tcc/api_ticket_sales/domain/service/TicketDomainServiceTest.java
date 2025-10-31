package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.HolderAlreadyHasTicketException;
import com.tcc.api_ticket_sales.domain.exception.InvalidAgeForEventException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.HolderFactory.createHolderEntityWithId;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.*;

class TicketDomainServiceTest {
    private TicketDomainService ticketDomainService;

    @BeforeEach
    void setUp() {
        ticketDomainService = new TicketDomainService();
    }

    @Test
    @Tag("unit")
    void createTicket_shouldCreateTicket_whenNoRestrictionAndHolderHasNoTickets() {
        // arrange: holder has no tickets and event has no restriction
        EventEntity eventEntity = createEventEntityWithId();
        eventEntity.setAgeRestriction(18);

        HolderEntity holderEntity = createHolderEntityWithId();
        holderEntity.setBirthDate(LocalDate.now().minusYears(19));
        holderEntity.setTicketEntities(null);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        ticketTypeEntity.setEventEntity(eventEntity);

        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        // act
        TicketEntity created = ticketDomainService.createTicket(ticketTypeEntity, holderEntity, orderEntity, paymentStatusEntity);

        // assert
        assertNotNull(created);
        assertEquals(ticketTypeEntity, created.getTicketTypeEntity());
        assertEquals(holderEntity, created.getHolderEntity());
        assertEquals(orderEntity, created.getOrderEntity());
        assertEquals(paymentStatusEntity, created.getPaymentStatusEntity());
    }

    @Test
    @Tag("unit")
    void createTicket_shouldThrowHolderAlreadyHasTicket_whenHolderHasApprovedTicketSameEvent() {
        // arrange: approved ticket for the same event
        EventEntity eventEntity = createEventEntityWithId();
        eventEntity.setAgeRestriction(0);

        PaymentStatusEntity approved = new PaymentStatusEntity();
        approved.setDescription(PaymentStatusEnum.APPROVED.getName());

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        ticketTypeEntity.setEventEntity(eventEntity);

        TicketEntity existingTicket = new TicketEntity();
        existingTicket.setPaymentStatusEntity(approved);
        existingTicket.setTicketTypeEntity(ticketTypeEntity);

        HolderEntity holderEntity = createHolderEntityWithId();
        holderEntity.setTicketEntities(List.of(existingTicket));

        OrderEntity orderEntity = createOrderEntityWithId();

        // act / assert
        assertThrows(HolderAlreadyHasTicketException.class, () ->
                ticketDomainService.createTicket(ticketTypeEntity, holderEntity, orderEntity, approved)
        );
    }


    @Test
    @Tag("unit")
    void createTicket_shouldThrowInvalidAgeForEvent_whenRestrictionAndHolderBirthDateInFuture() {
        EventEntity eventEntity = createEventEntityWithId();
        eventEntity.setAgeRestriction(10);

        HolderEntity holderEntity = createHolderEntityWithId();
        holderEntity.setBirthDate(LocalDate.now().minusDays(30));

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        ticketTypeEntity.setEventEntity(eventEntity);

        OrderEntity orderEntity = createOrderEntityWithId();

        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity();

        assertThrows(InvalidAgeForEventException.class, () ->
                ticketDomainService.createTicket(ticketTypeEntity, holderEntity, orderEntity, paymentStatusEntity)
        );
    }
}