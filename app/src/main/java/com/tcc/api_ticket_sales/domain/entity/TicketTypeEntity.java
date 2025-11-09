package com.tcc.api_ticket_sales.domain.entity;


import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.CascadeType;
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
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.tcc.api_ticket_sales.domain.utils.CheckDate.checkDateInitialGreaterThanDateFinal;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name= "ticket_types")
public class TicketTypeEntity extends Auditable{

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity eventEntity;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private LocalDateTime dateInitial;

    @Column(nullable = false)
    private LocalDateTime dateFinal;

    @OneToMany(mappedBy = "ticketTypeEntity", cascade = CascadeType.ALL)
    private List<TicketEntity> ticketEntities;

    private TicketTypeEntity(
            String name,
            String description,
            BigDecimal price,
            EventEntity eventEntity,
            int capacity,
            LocalDateTime dateInitial,
            LocalDateTime dateFinal
    ) {
        if(name == null || name.isBlank()){
            throw new BusinessException("Nome do ingresso inválido");
        }

        if(dateInitial == null){
            throw new BusinessException("Data inicial do ingresso inválida");
        }

        if(dateFinal == null){
            throw new BusinessException("Data final do ingresso inválida");
        }

        if(capacity <= 0){
            throw new BusinessException("Capacidade do ingresso inválida");
        }

        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException("Preço do ingresso inválido");
        }

        if(eventEntity == null){
            throw new BusinessException("Evento inválido para vincular o ingresso");
        }

        checkDateInitialGreaterThanDateFinal(dateInitial, dateFinal);

        this.name= name;
        this.description = description;
        this.price = price;
        this.eventEntity = eventEntity;
        this.capacity = capacity;
        this.dateInitial = dateInitial;
        this.dateFinal = dateFinal;
    }

    public static TicketTypeEntity of(String name,
                                      String description,
                                      BigDecimal price,
                                      EventEntity eventEntity,
                                      int capacity,
                                      LocalDateTime dateInitial,
                                      LocalDateTime dateFinal){
        return new TicketTypeEntity(name,
                description,
                price,
                eventEntity,
                capacity,
                dateInitial,
                dateFinal
        );
    }
}
