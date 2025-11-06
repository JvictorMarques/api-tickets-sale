package com.tcc.api_ticket_sales.domain.exception;

public class EventCapacityReductionNotAllowedException extends ConflictException {
    public EventCapacityReductionNotAllowedException(int capacityTotal) {

        super(
                String.format("A capacidade do evento não pode ser menor que a soma das capacidades dos tipos de ingresso já cadastrados: %d.", capacityTotal)
        );
    }
}
