package com.tcc.api_ticket_sales.domain.entity;


import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tcc.api_ticket_sales.domain.utils.CheckDate.checkDateInitialGreaterThanDateFinal;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name= "tickets")
public class TicketEntity extends Auditable{

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ticket_holder",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "holder_id")
    )
    private List<HolderEntity> holderEntities = new ArrayList<>();


    private TicketEntity(
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

        if(dateInitial == null || dateInitial.isBefore(LocalDateTime.now())){
            throw new BusinessException("Data inicial do ingresso inválida");
        }

        if(dateFinal == null || dateFinal.isBefore(LocalDateTime.now())){
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

    public static TicketEntity of(String name,
                                  String description,
                                  BigDecimal price,
                                  EventEntity eventEntity,
                                  int capacity,
                                  LocalDateTime dateInitial,
                                  LocalDateTime dateFinal){
        return new  TicketEntity(name,
                description,
                price,
                eventEntity,
                capacity,
                dateInitial,
                dateFinal);
    }
}
