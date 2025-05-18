package com.tcc.api_ticket_sales.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name= "ticket")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Builder.Default
    @Column(nullable = false)
    private boolean isEnable= true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_category_id")
    private TicketCategoryEntity ticketCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_holder_id")
    private TicketHolderEntity ticketHolderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity eventEntity;
}
