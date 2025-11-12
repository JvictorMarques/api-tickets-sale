package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "holders")
public class HolderEntity extends Auditable {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "holderEntity")
    private List<TicketEntity> ticketEntities;

    private HolderEntity(
            String name,
            String email,
            LocalDate birthDate
    ){
            if(name == null || name.isBlank()){
                throw new BusinessException("Nome do titular inválido");
            }

            if(email == null || email.isBlank()){
                throw new BusinessException("Email do titular inválido");
            }

            if(birthDate == null){
                throw new BusinessException("Data de nascimento do titular inválida");
            }

            this.name= name;
            this.email = email;
            this.birthDate = birthDate;
    }


    public static HolderEntity of(
            String name,
            String email,
            LocalDate birthDate
    ){
        return new HolderEntity(
                name,
                email,
                birthDate
        );
    }
}
