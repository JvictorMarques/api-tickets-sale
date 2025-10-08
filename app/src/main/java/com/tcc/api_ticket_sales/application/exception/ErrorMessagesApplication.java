package com.tcc.api_ticket_sales.application.exception;

public class ErrorMessagesApplication {

    private ErrorMessagesApplication(){
        throw new IllegalStateException("Utility class");
    }

    public static final String TICKET_NOT_FOUND = "Ingresso inválido.";
    public static final String HOLDER_NOT_FOUND = "Titular inválido.";
    public static final String EVENT_NOT_FOUND = "Evento inválido.";
    public static final String EVENT_UNAVAILABLE = "Evento indisponível.";
    public static final String EVENT_ALREADY_EXISTS = "Evento existente.";
;}
