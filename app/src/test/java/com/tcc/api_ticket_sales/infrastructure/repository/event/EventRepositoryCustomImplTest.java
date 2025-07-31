package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.entity.EventCategoryEntity;
import com.tcc.api_ticket_sales.entity.EventEntity;
import com.tcc.api_ticket_sales.entity.EventLocationEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.impl.EventRepositoryCustomImpl;
import com.tcc.api_ticket_sales.application.model.EventModel;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
class EventRepositoryCustomImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventRepositoryCustomImpl repository;


    @Test
    @DisplayName("Should return events from the database according to the initial and final data")
    void getEventsFilterDate() {

        EventEntity event1 = EventEntity.builder()
                .name("Evento Teste")
                .dateInitial(LocalDateTime.now().minusMonths(1))
                .dateFinal(LocalDateTime.now().minusMonths(1))
                .build();
        EventEntity event2 = EventEntity.builder()
                .name("Evento Teste 2")
                .dateInitial(LocalDateTime.now())
                .dateFinal(LocalDateTime.now().plusMonths(1))
                .build();

        entityManager.persist(event1);
        entityManager.persist(event2);


        FilterEventRequestDTO filter = new FilterEventRequestDTO(LocalDate.now(), LocalDate.now(), null, null);

        List<EventModel> events = repository.getEventsByFilter(filter);
        assertThat(events.size() == 1
                && events.stream().anyMatch((e) -> e.getName() == event2.getName())
        ).isTrue();
    }

    @Test
    @DisplayName("Should return events from the database according to the category")
    void getEventsFilterCategory() {
        EventCategoryEntity category1 = EventCategoryEntity.builder().name("Category 1").build();
        EventCategoryEntity category2 = EventCategoryEntity.builder().name("Category 2").build();
        entityManager.persist(category1);
        entityManager.persist(category2);

        EventEntity event1 = EventEntity.builder()
                .name("Evento Teste")
                .eventCategoryEntity(category1)
                .build();
        EventEntity event2 = EventEntity.builder()
                .name("Evento Teste 2")
                .eventCategoryEntity(category2)
                .build();

        entityManager.persist(event1);
        entityManager.persist(event2);

        FilterEventRequestDTO filter = new FilterEventRequestDTO(null, null, List.of(category2.getId()), null);
        List<EventModel> events = repository.getEventsByFilter(filter);

        assertThat(events.size() == 1
                && events.stream().anyMatch((e) -> e.getName() == event2.getName())
        ).isTrue();
    }

    @Test
    @DisplayName("Should return events from the database according to the location")
    void getEventsFilterLocation() {
        EventLocationEntity location1 = EventLocationEntity.builder().name("Location 1").build();
        EventLocationEntity location2 = EventLocationEntity.builder().name("Location 2").build();
        entityManager.persist(location1);
        entityManager.persist(location2);

        EventEntity event1 = EventEntity.builder()
                .name("Evento Teste")
                .eventLocationEntity(location1)
                .build();
        EventEntity event2 = EventEntity.builder()
                .name("Evento Teste 2")
                .eventLocationEntity(location2)
                .build();

        entityManager.persist(event1);
        entityManager.persist(event2);

        FilterEventRequestDTO filter = new FilterEventRequestDTO(null, null, null, List.of(location2.getId()));
        List<EventModel> events = repository.getEventsByFilter(filter);

        assertThat(events.size() == 1
                && events.stream().anyMatch((e) -> e.getName() == event2.getName())
        ).isTrue();
    }

    @Test
    @DisplayName("Should return events from the database according to the all filters")
    void getEventsFilterALl() {
        EventCategoryEntity category1 = EventCategoryEntity.builder().name("Category 1").build();
        EventCategoryEntity category2 = EventCategoryEntity.builder().name("Category 2").build();
        entityManager.persist(category1);
        entityManager.persist(category2);

        EventLocationEntity location1 = EventLocationEntity.builder().name("Location 1").build();
        EventLocationEntity location2 = EventLocationEntity.builder().name("Location 2").build();
        entityManager.persist(location1);
        entityManager.persist(location2);

        EventEntity event1 = EventEntity.builder()
                .name("Evento Teste")
                .dateInitial(LocalDateTime.now().minusMonths(1))
                .dateFinal(LocalDateTime.now().minusMonths(1))
                .eventCategoryEntity(category1)
                .eventLocationEntity(location1)
                .build();
        EventEntity event2 = EventEntity.builder()
                .name("Evento Teste 2")
                .dateInitial(LocalDateTime.now())
                .dateFinal(LocalDateTime.now().plusMonths(1))
                .eventCategoryEntity(category2)
                .eventLocationEntity(location2)
                .build();
        EventEntity event3 = EventEntity.builder()
                .name("Evento Teste 3")
                .dateInitial(LocalDateTime.now())
                .dateFinal(LocalDateTime.now().plusMonths(1))
                .eventCategoryEntity(category2)
                .eventLocationEntity(location1)
                .build();

        entityManager.persist(event1);
        entityManager.persist(event2);
        entityManager.persist(event3);

        FilterEventRequestDTO filter = new FilterEventRequestDTO(LocalDate.now(), LocalDate.now(), List.of(category2.getId()), List.of(location1.getId()));
        List<EventModel> events = repository.getEventsByFilter(filter);

        assertThat(events.size() == 1
                && events.stream().anyMatch((e) -> e.getName() == event3.getName())
        ).isTrue();
    }

    @Test
    @DisplayName("Should return events from the database without filters")
    void getEventsWithoutFilters() {

        EventEntity event1 = EventEntity.builder()
                .name("Evento Teste")
                .dateInitial(LocalDateTime.now().minusMonths(1))
                .dateFinal(LocalDateTime.now().minusMonths(1))
                .build();
        EventEntity event2 = EventEntity.builder()
                .name("Evento Teste 2")
                .dateInitial(LocalDateTime.now())
                .dateFinal(LocalDateTime.now().plusMonths(1))
                .build();

        entityManager.persist(event1);
        entityManager.persist(event2);

        FilterEventRequestDTO filter = new FilterEventRequestDTO(null, null, null, null);
        List<EventModel> events = repository.getEventsByFilter(filter);

        assertThat(events.size() == 2).isTrue();
    }

    @Test
    @DisplayName("Should return list empty")
    void getEventsDataBaseEmpty() {
        FilterEventRequestDTO filter = new FilterEventRequestDTO(null, null, null, null);
        List<EventModel> events = repository.getEventsByFilter(filter);

        assertThat(events.isEmpty()).isTrue();
    }
}
