package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.application.dto.event.EventFilterRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventSpecificationFactory {
    public Specification<EventEntity> findConflictingEvents (EventEntity eventEntity){

        return EventSpecification.nameEquals(eventEntity.getName())
                .and(EventSpecification.locationEquals(eventEntity.getLocation()))
                .and(EventSpecification.dateBetween(eventEntity.getDateInitial(), eventEntity.getDateFinal()))
                .and(EventSpecification.idNotEquals(eventEntity.getId()))
                .and(EventSpecification.deletedAtIsNull());
    }

    public Specification<EventEntity> findFilter (EventFilterRequestDTO filter){

        Specification<EventEntity> specification = EventSpecification.nameContains(filter.getName())
                .and(EventSpecification.locationContains(filter.getLocation()))
                .and(EventSpecification.dateBetween(filter.getDateInitial(), filter.getDateFinal()))
                .and(EventSpecification.ageRestrictionLessThanOrEqual(filter.getAgeRestriction()));

        if(filter.getAvailable() != null && filter.getAvailable()){
            return specification.and(EventSpecification.capacityGreatThanTicketPurchased())
                    .and(EventSpecification.notClosed());
        }

        return specification.and(EventSpecification.deletedAtIsNull());
    }
}
