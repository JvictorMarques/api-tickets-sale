package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;


public class EventSpecification {
    public static Specification<EventEntity> nameEquals(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), name.toLowerCase());
    }

    public static Specification<EventEntity> locationEquals(String location) {
        return (root, query, cb) ->
                location == null ? null : cb.equal(cb.lower(root.get("location")), location.toLowerCase());
    }

    public static Specification<EventEntity> dateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if(start == null || end == null) return null;
            return cb.or(
                    cb.between(root.get("dateInitial"), start, end),
                    cb.between(root.get("dateFinal"), start, end),
                    cb.and(
                            cb.lessThanOrEqualTo(root.get("dateInitial"), start),
                            cb.greaterThanOrEqualTo(root.get("dateFinal"), end)
                    )
            );
        };
    }

    public static Specification<EventEntity> idEquals(UUID id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("id"), id);
    }
}
