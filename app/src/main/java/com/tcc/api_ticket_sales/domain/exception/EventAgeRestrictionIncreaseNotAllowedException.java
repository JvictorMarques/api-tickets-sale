package com.tcc.api_ticket_sales.domain.exception;

public class EventAgeRestrictionIncreaseNotAllowedException extends ConflictException {
    public EventAgeRestrictionIncreaseNotAllowedException() {
        super(
                "O evento possui ingressos já vendidos, portanto, a restrição de idade não pode ser aumentada."
        );
    }
}
