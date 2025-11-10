package com.tcc.api_ticket_sales.infrastructure.repository.holder;

import com.tcc.api_ticket_sales.BaseIntegrationTest;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.HolderFactory.createHolderEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithoutId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class HolderSpecificationTest extends BaseIntegrationTest {
    @Autowired
    private HolderRepository holderRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Tag("integration")
    @Test
    void byTicketType_ShouldReturnListHolders_WhenTicketTypeExistsAndPaymentApprovedAndHolderNoDeleted() {
        HolderEntity holderEntity = createHolderEntityWithoutId();
        holderRepository.save(holderEntity);

        EventEntity eventEntity = createEventEntityWithoutId();
        eventRepository.save(eventEntity);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId(eventEntity);
        ticketTypeRepository.save(ticketTypeEntity);

        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity(
                PaymentStatusEnum.APPROVED.getName()
        );
        paymentStatusRepository.save(paymentStatusEntity);

        OrderEntity orderEntity = createOrderEntityWithoutId();
        orderRepository.save(orderEntity);

        TicketEntity ticketEntity = new TicketEntity(
                ticketTypeEntity,
                holderEntity,
                orderEntity,
                paymentStatusEntity
        );
        ticketRepository.save(ticketEntity);

        Specification<HolderEntity> specification = HolderSpecification.byTicketType(
          ticketTypeEntity.getId()
        );
        List<HolderEntity> holderEntities = holderRepository.findAll(specification);

        assertEquals(1, holderEntities.size());
        assertEquals(holderEntity.getId(), holderEntities.getFirst().getId());
    }

    @Tag("integration")
    @Test
    void byTicketType_ShouldReturnListHoldersEmpty_WhenPaymentStatusIsNotApproved() {
        HolderEntity holderEntity = createHolderEntityWithoutId();
        holderRepository.save(holderEntity);

        EventEntity eventEntity = createEventEntityWithoutId();
        eventRepository.save(eventEntity);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId(eventEntity);
        ticketTypeRepository.save(ticketTypeEntity);

        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity(
                PaymentStatusEnum.CANCELLED.getName()
        );
        paymentStatusRepository.save(paymentStatusEntity);

        OrderEntity orderEntity = createOrderEntityWithoutId();
        orderRepository.save(orderEntity);

        TicketEntity ticketEntity = new TicketEntity(
                ticketTypeEntity,
                holderEntity,
                orderEntity,
                paymentStatusEntity
        );
        ticketRepository.save(ticketEntity);

        Specification<HolderEntity> specification = HolderSpecification.byTicketType(
                ticketTypeEntity.getId()
        );
        List<HolderEntity> holderEntities = holderRepository.findAll(specification);

        assertTrue(holderEntities.isEmpty());
    }

    @Tag("integration")
    @Test
    void byTicketType_ShouldReturnListHoldersEmpty_WhenHolderDeleted() {
        HolderEntity holderEntity = createHolderEntityWithoutId();
        holderEntity.setDeletedAt(LocalDateTime.now());
        holderRepository.save(holderEntity);

        EventEntity eventEntity = createEventEntityWithoutId();
        eventRepository.save(eventEntity);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId(eventEntity);
        ticketTypeRepository.save(ticketTypeEntity);

        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity(
                PaymentStatusEnum.APPROVED.getName()
        );
        paymentStatusRepository.save(paymentStatusEntity);

        OrderEntity orderEntity = createOrderEntityWithoutId();
        orderRepository.save(orderEntity);

        TicketEntity ticketEntity = new TicketEntity(
                ticketTypeEntity,
                holderEntity,
                orderEntity,
                paymentStatusEntity
        );
        ticketRepository.save(ticketEntity);

        Specification<HolderEntity> specification = HolderSpecification.byTicketType(
                ticketTypeEntity.getId()
        );
        List<HolderEntity> holderEntities = holderRepository.findAll(specification);

        assertTrue(holderEntities.isEmpty());
    }

    @Tag("integration")
    @Test
    void byTicketType_ShouldReturnListHoldersEmpty_WhenTicketIsDeleted() {
        HolderEntity holderEntity = createHolderEntityWithoutId();
        holderRepository.save(holderEntity);

        EventEntity eventEntity = createEventEntityWithoutId();
        eventRepository.save(eventEntity);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId(eventEntity);
        ticketTypeRepository.save(ticketTypeEntity);

        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity(
                PaymentStatusEnum.APPROVED.getName()
        );
        paymentStatusRepository.save(paymentStatusEntity);

        OrderEntity orderEntity = createOrderEntityWithoutId();
        orderRepository.save(orderEntity);

        TicketEntity ticketEntity = new TicketEntity(
                ticketTypeEntity,
                holderEntity,
                orderEntity,
                paymentStatusEntity
        );
        ticketEntity.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticketEntity);

        Specification<HolderEntity> specification = HolderSpecification.byTicketType(
                ticketTypeEntity.getId()
        );
        List<HolderEntity> holderEntities = holderRepository.findAll(specification);

        assertTrue(holderEntities.isEmpty());
    }

    @Tag("integration")
    @Test
    void byTicketType_ShouldReturnListHoldersEmpty_WhenNoHoldersExistForTicketType() {
        HolderEntity holderEntity = createHolderEntityWithoutId();
        holderRepository.save(holderEntity);

        EventEntity eventEntity = createEventEntityWithoutId();
        eventRepository.save(eventEntity);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId(eventEntity);
        ticketTypeRepository.save(ticketTypeEntity);

        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity(
                PaymentStatusEnum.APPROVED.getName()
        );
        paymentStatusRepository.save(paymentStatusEntity);

        OrderEntity orderEntity = createOrderEntityWithoutId();
        orderRepository.save(orderEntity);

        TicketEntity ticketEntity = new TicketEntity(
                ticketTypeEntity,
                holderEntity,
                orderEntity,
                paymentStatusEntity
        );
        ticketRepository.save(ticketEntity);

        Specification<HolderEntity> specification = HolderSpecification.byTicketType(
                UUID.randomUUID()
        );
        List<HolderEntity> holderEntities = holderRepository.findAll(specification);

        assertTrue(holderEntities.isEmpty());
    }
}