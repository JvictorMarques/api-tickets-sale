package com.tcc.api_ticket_sales.application.exception;

public class ErrorMessages {
    public static final String DATE_INITIAL_GREATER_THAN_DATE_FINAL = "Data inicial maior que data final.";
    public static final String DATE_INVALID = "Data inválida.";
    public static final String DATE_FINAL_IS_BEFORE_TODAY = "A data final informada no filtro é anterior a data atual.";
    public static final String TICKET_NOT_FOUND = "Ingresso inválido.";
    public static final String HOLDER_NOT_FOUND = "Titular inválido.";
    public static final String EVENT_NOT_FOUND = "Evento inválido.";
    public static final String EVENT_UNAVAILABLE = "Evento indisponível. Quantidade de ingressos disponíveis esgotada.";
;}
