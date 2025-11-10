package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.application.dto.event.EventFilterRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventFilterRequestDTO;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventSpecificationFactoryTest {

    private final EventSpecificationFactory eventSpecificationFactory = new EventSpecificationFactory();

    @Tag("Unit")
    @Test
    void findConflictingEvents_shouldReturnSpecification_whenAllFieldsArePresent() {
        // Arrange
        EventEntity event = createEventEntityWithId();

        // Act
        Specification<EventEntity> spec = eventSpecificationFactory.findConflictingEvents(event);

        // Assert
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void findConflictingEvents_shouldHandleNullFieldsGracefully() {
        // Arrange
        EventEntity event = createEventEntityWithId();
        event.setId(null);
        event.setName(null);
        event.setLocation(null);
        event.setDateInitial(null);
        event.setDateFinal(null);

        // Act
        Specification<EventEntity> spec = eventSpecificationFactory.findConflictingEvents(event);

        // Assert
        assertNotNull(spec);
    }

    @Test
    void findFilter_shouldReturnSpecification_whenAllFieldsAreFilledAndAvailableIsFalse() {
        // Arrange
        EventFilterRequestDTO filter = createEventFilterRequestDTO();
        filter.setAvailable(true);

        // Act
        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);

        // Assert
        assertNotNull(spec);
    }

    @Test
    void findFilter_shouldReturnSpecification_whenAvailableIsTrue() {
        // Arrange
        EventFilterRequestDTO filter = createEventFilterRequestDTO();
        filter.setAvailable(false);

        // Act
        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);

        // Assert
        assertNotNull(spec);
    }

    @Test
    void findFilter_shouldHandleNullFieldsGracefully() {
        // Arrange
        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setName(null);
        filter.setLocation(null);
        filter.setDateInitial(null);
        filter.setDateFinal(null);
        filter.setAgeRestriction(null);
        filter.setAvailable(null);

        // Act
        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);

        // Assert
        assertNotNull(spec);
    }
}
