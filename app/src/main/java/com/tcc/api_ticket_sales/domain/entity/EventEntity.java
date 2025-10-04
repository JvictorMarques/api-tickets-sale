package com.tcc.api_ticket_sales.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class EventEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateInitial = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateFinal = LocalDateTime.now();

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int ageRestriction;

    private String location;

    @OneToMany(mappedBy = "eventEntity")
    private List<TicketEntity> ticketEntities;
}
