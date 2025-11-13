package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;


public class EventSpecification {
    public static Specification<EventEntity> nameEquals(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), name.toLowerCase());
    }

    public static Specification<EventEntity> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<EventEntity> locationEquals(String location) {
        return (root, query, cb) ->
                location == null ? null : cb.equal(cb.lower(root.get("location")), location.toLowerCase());
    }

    public static Specification<EventEntity> locationContains(String location) {
        return (root, query, cb) ->
                location == null ? null : cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
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

    public static Specification<EventEntity> idNotEquals(UUID id) {
        return (root, query, cb) ->
                id == null ? null : cb.notEqual(root.get("id"), id);
    }

    public static Specification<EventEntity> ageRestrictionLessThanOrEqual(Integer ageRestriction){
        return (root, query, cb) ->
                ageRestriction == null ? null : cb.lessThanOrEqualTo(root.get("ageRestriction"), ageRestriction);
    }

    public static Specification<EventEntity> capacityGreatThanTicketPurchased(){
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<TicketEntity> rootSubquery = subquery.from(TicketEntity.class);
            Join<TicketEntity, TicketTypeEntity> joinTicketTypeEntity = rootSubquery.join("ticketTypeEntity", JoinType.INNER);

            subquery.select(cb.count(rootSubquery))
                    .where(cb.and(
                            cb.equal(joinTicketTypeEntity.get("eventEntity").get("id"), root.get("id")),
                            cb.isNull(joinTicketTypeEntity.get("deletedAt")),
                            cb.equal(rootSubquery.get("paymentStatusEntity").get("description"), PaymentStatusEnum.APPROVED.getName())
                            )
                    );

            return cb.and(
                    cb.greaterThan(root.get("capacity"), subquery)
            );
        };
    }

    public static Specification<EventEntity> deletedAtIsNull(){
        return (root, query, cb) ->
                cb.isNull(root.get("deletedAt"));
    }

    public static Specification<EventEntity> notClosed(){
        return (root, query, cb) ->
                cb.and(
                        cb.isNull(root.get("deletedAt")),
                        cb.greaterThanOrEqualTo(root.get("dateFinal"), LocalDateTime.now())
                );
    }
}
