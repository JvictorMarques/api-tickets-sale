package com.tcc.api_ticket_sales.infrastructure.repository.holder;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class HolderSpecification {

    public static Specification<HolderEntity> byTicketType(UUID ticketTypeId){
        return (root, query, cb) -> {
            Join<HolderEntity, TicketEntity> joinTicket = root.join("ticketEntities", JoinType.INNER);

            return cb.and(
                    cb.equal(joinTicket.get("ticketTypeEntity").get("id"), ticketTypeId),
                    cb.isNull(joinTicket.get("deletedAt")),
                    cb.equal(joinTicket.get("paymentStatusEntity").get("description"), PaymentStatusEnum.APPROVED.getName()),
                    cb.isNull(root.get("deletedAt"))
                    );
        };
    }
}
