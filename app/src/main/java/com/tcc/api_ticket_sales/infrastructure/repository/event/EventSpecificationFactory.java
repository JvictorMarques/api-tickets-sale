package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EventSpecificationFactory {
    public Specification<EventEntity> findConflictingEvents (EventEntity eventEntity){

        return EventSpecification.nameEquals(eventEntity.getName())
                .and(EventSpecification.locationEquals(eventEntity.getLocation()))
                .and(EventSpecification.dateBetween(eventEntity.getDateInitial(), eventEntity.getDateFinal()))
                .and(EventSpecification.idEquals(eventEntity.getId()));
    }
}
