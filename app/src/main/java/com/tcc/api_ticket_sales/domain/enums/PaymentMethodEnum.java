package com.tcc.api_ticket_sales.domain.enums;

public enum PaymentMethodEnum {
    PIX("pix"),
    CARD("card"),
    TICKET("ticket");


    private final String name;

    PaymentMethodEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
