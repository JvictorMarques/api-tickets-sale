package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.tcc.api_ticket_sales.domain.utils.CheckDate.checkDateInitialGreaterThanDateFinal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) // para JPA
@Entity
@Table(name = "events")
public class EventEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDateTime dateInitial;

    @Column(nullable = false)
    private LocalDateTime dateFinal;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int ageRestriction;

    private String location;

    @OneToMany(mappedBy = "eventEntity")
    private List<TicketTypeEntity> ticketTypeEntities;


    private EventEntity(
            String name,
            String description,
            LocalDateTime dateInitial,
            LocalDateTime dateFinal,
            int capacity,
            int ageRestriction,
            String location
    ){
        if(name == null || name.isBlank()){
            throw new BusinessException("Nome do evento inválido");
        }

        if(location == null || location.isBlank()){
            throw new BusinessException("Local do evento inválido");
        }

        if(dateInitial == null){
            throw new BusinessException("Data inicial do evento inválida");
        }

        if(dateFinal == null){
            throw new BusinessException("Data final do evento inválida");
        }

        if(capacity <= 0){
            throw new BusinessException("Capacidade do evento inválida");
        }

        checkDateInitialGreaterThanDateFinal(dateInitial, dateFinal);

        this.name= name;
        this.description = description;
        this.dateInitial = dateInitial;
        this.dateFinal = dateFinal;
        this.capacity = capacity;
        this.ageRestriction = ageRestriction;
        this.location = location;
    }

    public static EventEntity of(String name,
                                 String description,
                                 LocalDateTime dateInitial,
                                 LocalDateTime dateFinal,
                                 int capacity,
                                 int ageRestriction,
                                 String location) {
        return new EventEntity(
                name,
                description,
                dateInitial,
                dateFinal,
                capacity,
                ageRestriction,
                location
        );
    }

    public static EventEntity reference(UUID id) {
        EventEntity e = new EventEntity();
        e.id = id;
        return e;
    }

    public boolean isClosed(){
        return (this.getDeletedAt() != null || this.dateFinal.isBefore(LocalDateTime.now()));
    }
}
