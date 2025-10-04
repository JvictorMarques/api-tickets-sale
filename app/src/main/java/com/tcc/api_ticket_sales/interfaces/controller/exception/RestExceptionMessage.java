package com.tcc.api_ticket_sales.interfaces.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RestExceptionMessage {
    private String message;
    private int status;
    private String path;
    private LocalDateTime timeStamp;
    private List<String> errors = List.of();
}
