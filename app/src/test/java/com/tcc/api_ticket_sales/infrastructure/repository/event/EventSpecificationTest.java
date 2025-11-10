package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class EventSpecificationTest {

    @Mock
    private Root<EventEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Tag("unit")
    @Test
    void nameEquals_shouldReturnNull_whenNameIsNull() {
        Specification<EventEntity> specification = EventSpecification.nameEquals(null);
        assertNotNull(specification);

        assertNull(specification.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void nameEquals_shouldReturnSpecification_whenNameIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.nameEquals("Show");
        assertNotNull(spec);

        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void nameContains_shouldReturnNull_whenNameIsNull() {
        Specification<EventEntity> specification = EventSpecification.nameContains(null);
        assertNotNull(specification);

        assertNull(specification.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void nameContains_shouldReturnSpecification_whenNameIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.nameContains("Show");
        assertNotNull(spec);

        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void locationEquals_shouldReturnNull_whenLocationIsNull() {
        Specification<EventEntity> spec = EventSpecification.locationEquals(null);
        assertNotNull(spec);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Test
    void locationEquals_shouldReturnPredicate_whenLocationIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.locationEquals("São Paulo");
        assertNotNull(spec);
        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void locationContains_shouldReturnNull_whenLocationIsNull() {
        Specification<EventEntity> spec = EventSpecification.locationContains(null);
        assertNotNull(spec);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Test
    void locationContains_shouldReturnPredicate_whenLocationIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.locationContains("Rio");
        assertNotNull(spec);
        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
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
        Specification<EventEntity> spec1 = EventSpecification.dateBetween(null, LocalDateTime.now());
        Specification<EventEntity> spec2 = EventSpecification.dateBetween(LocalDateTime.now(), null);

        assertNotNull(spec1);
        assertNotNull(spec2);

        assertNull(spec1.toPredicate(root, query, cb));
        assertNull(spec2.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void dateBetween_shouldReturnPredicate_whenDatesAreNotNull() {
        Specification<EventEntity> spec = EventSpecification.dateBetween(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        assertNotNull(spec);
        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void idEquals_shouldReturnNull_whenIdIsNull() {
        Specification<EventEntity> spec = EventSpecification.idEquals(null);
        assertNotNull(spec);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void idEquals_shouldReturnPredicate_whenIdIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.idEquals(UUID.randomUUID());
        assertNotNull(spec);
        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void idNotEquals_shouldReturnNull_whenIdIsNull() {
        Specification<EventEntity> spec = EventSpecification.idNotEquals(null);
        assertNotNull(spec);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void idNotEquals_shouldReturnPredicate_whenIdIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.idNotEquals(UUID.randomUUID());
        assertNotNull(spec);
        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void ageRestrictionLessThanOrEqual_shouldReturnNull_whenAgeIsNull() {
        Specification<EventEntity> spec = EventSpecification.ageRestrictionLessThanOrEqual(null);
        assertNotNull(spec);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void ageRestrictionLessThanOrEqual_shouldReturnPredicate_whenAgeIsNotNull() {
        Specification<EventEntity> spec = EventSpecification.ageRestrictionLessThanOrEqual(18);
        assertNotNull(spec);
        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
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

        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }

    @Tag("unit")
    @Test
    void notClosed_shouldReturnSpecification() {
        Specification<EventEntity> spec = EventSpecification.notClosed();
        assertNotNull(spec);

        assertDoesNotThrow(() -> spec.toPredicate(root, query, cb));
    }
}
