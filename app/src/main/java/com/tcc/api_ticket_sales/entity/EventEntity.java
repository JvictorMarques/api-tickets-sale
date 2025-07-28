package com.tcc.api_ticket_sales.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateInitial = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateFinal = LocalDateTime.now();

    private Integer maxPeople;

    @Builder.Default
    @Column(nullable = false)
    private boolean isAdultOnly = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_category_id")
    private EventCategoryEntity eventCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_location_id")
    private EventLocationEntity eventLocationEntity;

    @OneToMany(mappedBy = "eventEntity")
    private List<TicketEntity> ticketEntities;
}
