package com.tcc.api_ticket_sales.domain.exception;

public class InvalidAgeForEventException extends BusinessException {
    public InvalidAgeForEventException(String message) {

        super(
                String.format("O titular %s ingresso não possue a idade necessária para o evento.", message)
        );
    }
}
