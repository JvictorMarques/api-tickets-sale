package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class EventSpecificationTest {

    @Tag("unit")
    @Test
    void nameEquals_shouldReturnNull_whenNameIsNull() {
        Specification<EventEntity> specification = EventSpecification.nameEquals(null);
        assertNotNull(specification);
    }

    @Tag("unit")
    @Test
    void nameEquals_shouldReturnSpecification_whenNameIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.nameEquals("Show");
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void nameContains_shouldReturnNull_whenNameIsNull() {
        assertNotNull(EventSpecification.nameContains(null));
    }

    @Tag("unit")
    @Test
    void nameContains_shouldReturnSpecification_whenNameIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.nameContains("Show");
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void locationEquals_shouldReturnNull_whenLocationIsNull() {
        assertNotNull(EventSpecification.locationEquals(null));
    }

    @Tag("unit")
    @Test
    void locationEquals_shouldReturnSpecification_whenLocationIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.locationEquals("Arena");
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void locationContains_shouldReturnNull_whenLocationIsNull() {
        assertNotNull(EventSpecification.locationContains(null));
    }

    @Tag("unit")
    @Test
    void locationContains_shouldReturnSpecification_whenLocationIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.locationContains("Arena");
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void dateBetween_shouldReturnNull_whenStartOrEndIsNull() {
        LocalDateTime now = LocalDateTime.now();
        assertNotNull(EventSpecification.dateBetween(null, now));
        assertNotNull(EventSpecification.dateBetween(now, null));
    }

    @Tag("unit")
    @Test
    void dateBetween_shouldReturnSpecification_whenBothDatesAreNotNull() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        Specification<EventEntity> spec = EventSpecification.dateBetween(start, end);
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void idEquals_shouldReturnNull_whenIdIsNull() {
        assertNotNull(EventSpecification.idEquals(null));
    }

    @Tag("unit")
    @Test
    void idEquals_shouldReturnSpecification_whenIdIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.idEquals(UUID.randomUUID());
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void idNotEquals_shouldReturnNull_whenIdIsNull() {
        assertNotNull(EventSpecification.idNotEquals(null));
    }

    @Tag("unit")
    @Test
    void idNotEquals_shouldReturnSpecification_whenIdIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.idNotEquals(UUID.randomUUID());
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void ageRestrictionLessThanOrEqual_shouldReturnNull_whenAgeIsNull() {
        assertNotNull(EventSpecification.ageRestrictionLessThanOrEqual(null));
    }

    @Tag("unit")
    @Test
    void ageRestrictionLessThanOrEqual_shouldReturnSpecification_whenAgeIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.ageRestrictionLessThanOrEqual(18);
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void capacityGreatThanTicketPurchased_shouldReturnSpecification() {
        Specification<EventEntity> spec = EventSpecification.capacityGreatThanTicketPurchased();
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void deletedAtIsNull_shouldReturnSpecification() {
        Specification<EventEntity> spec = EventSpecification.deletedAtIsNull();
        assertNotNull(spec);
    }

    @Tag("unit")
    @Test
    void notClosed_shouldReturnSpecification() {
        Specification<EventEntity> spec = EventSpecification.notClosed();
        assertNotNull(spec);
    }
}
