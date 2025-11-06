package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.service.spi.EventActionWithParameter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<EventEntity> checkExists(String name, String location, LocalDateTime dateInitial, LocalDateTime dateFinal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(cb.lower(root.get("name")), name.toLowerCase()));
        predicates.add(cb.equal(cb.lower(root.get("location")), location.toLowerCase()));
        predicates.add(cb.isNull(root.get("deletedAt")));

        predicates.add(cb.or(
                cb.between(root.get("dateInitial"), dateInitial, dateFinal),
                cb.between(root.get("dateFinal"), dateInitial, dateFinal),
                cb.and(
                        cb.lessThanOrEqualTo(root.get("dateInitial"), dateInitial),
                        cb.greaterThanOrEqualTo(root.get("dateFinal"), dateFinal)
                )
        ));

        query.select(root)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

}
