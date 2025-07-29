package com.tcc.api_ticket_sales.infrastructure.repository.event.impl;

import com.tcc.api_ticket_sales.entity.EventCategoryEntity;
import com.tcc.api_ticket_sales.entity.EventEntity;
import com.tcc.api_ticket_sales.entity.EventLocationEntity;
import com.tcc.api_ticket_sales.entity.HolderEntity;
import com.tcc.api_ticket_sales.entity.TicketEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepositoryCustom;
import com.tcc.api_ticket_sales.application.model.EventModel;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<EventModel> getEventsByFilter(FilterEventRequestDTO filter){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<EventModel> query = cb.createQuery(EventModel.class);

        Root<EventEntity> eventEntityRoot = query.from(EventEntity.class);

        Join<EventEntity, EventCategoryEntity> joinEventCategory = eventEntityRoot.join("eventCategoryEntity", JoinType.LEFT);
        Join<EventEntity, EventLocationEntity> joinEventLocation = eventEntityRoot.join("eventLocationEntity", JoinType.LEFT);
        Join<EventEntity, TicketEntity> joinTicket = eventEntityRoot.join("ticketEntities", JoinType.LEFT);
        Join<TicketEntity, HolderEntity> joinHolder = joinTicket.join("holderEntities", JoinType.LEFT);

        Expression<Long> numberTicketsUnavailable = cb.count(joinHolder.get("id"));
        Expression<Long> numberTicketsAvailable = cb.diff(eventEntityRoot.get("maxPeople"), numberTicketsUnavailable);

        List<Predicate> predicates = new ArrayList<>();

        // Filtros
        if(filter != null){
            if(filter.dateInitial() != null && filter.dateFinal() != null){
                LocalDateTime dateInitialTime = filter.dateInitial().atStartOfDay(); // 00:00:00
                LocalDateTime dateFinalTime = filter.dateFinal().atTime(LocalTime.MAX); //

                predicates.add(
                        cb.or(
                                cb.between(eventEntityRoot.get("dateInitial"), dateInitialTime, dateFinalTime),
                                cb.between(eventEntityRoot.get("dateFinal"), dateInitialTime, dateFinalTime)
                        )
                );
            }

            if(filter.categoriesId() != null){
                predicates.add(
                        joinEventCategory.get("id").in(filter.categoriesId())
                );
            }

            if(filter.locationsId() != null){
                predicates.add(
                        joinEventLocation.get("id").in(filter.locationsId())
                );
            }
        }

        query.select(cb.construct(EventModel.class,
                eventEntityRoot.get("id"),
                eventEntityRoot.get("name"),
                eventEntityRoot.get("dateInitial"),
                eventEntityRoot.get("dateFinal"),
                eventEntityRoot.get("isAdultOnly"),
                joinEventCategory.get("name"),
                joinEventLocation.get("name"),
                eventEntityRoot.get("maxPeople"),
                numberTicketsUnavailable,
                numberTicketsAvailable
        ));
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        query.groupBy(
                eventEntityRoot.get("id"),
                joinEventCategory.get("name"),
                joinEventLocation.get("name"),
                joinHolder.get("id")
        );

        query.orderBy(cb.asc(eventEntityRoot.get("name")));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Long getTicketsAvailableByEventId(UUID eventId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<EventEntity> eventEntityRoot = query.from(EventEntity.class);
        Join<EventEntity, TicketEntity> joinTicket = eventEntityRoot.join("ticketEntities", JoinType.LEFT);
        Join<TicketEntity, HolderEntity> joinHolder = joinTicket.join("holderEntities", JoinType.LEFT);

        Expression<Long> numberTicketsUnavailable = cb.count(joinHolder.get("id"));
        Expression<Long> numberTicketsAvailable = cb.diff(eventEntityRoot.get("maxPeople"), numberTicketsUnavailable);

        List<Predicate> predicates = new ArrayList<>();



        query.select(cb.construct(Long.class,
                numberTicketsAvailable
        ));
        query.where(cb.equal(eventEntityRoot.get("id"), eventId));

        query.groupBy(
                eventEntityRoot.get("id")
        );

        query.orderBy(cb.asc(eventEntityRoot.get("name")));
        return entityManager.createQuery(query).getSingleResult();
    }
}
