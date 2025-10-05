package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public boolean checkExists(EventCreateDTO event) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<EventEntity> root = query.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(cb.lower(root.get("name")), event.getName().toLowerCase()));
        predicates.add(cb.equal(cb.lower(root.get("location")), event.getLocation().toLowerCase()));

        predicates.add(cb.or(
                cb.between(root.get("dateInitial"), event.getDateInitial(), event.getDateFinal()),
                cb.between(root.get("dateFinal"), event.getDateInitial(), event.getDateFinal()),
                cb.and(
                        cb.lessThanOrEqualTo(root.get("dateInitial"), event.getDateInitial()),
                        cb.greaterThanOrEqualTo(root.get("dateFinal"), event.getDateFinal())
                )
        ));

        query.select(cb.count(root))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        Long count = entityManager.createQuery(query).getSingleResult();

        return count > 0;
    }
}
