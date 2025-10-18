package com.tcc.api_ticket_sales.domain.enums;

public enum PaymentStatusEnum {
    APPROVED("approved");

    private final String name;

    PaymentStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
