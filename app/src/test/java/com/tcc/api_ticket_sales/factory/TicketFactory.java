package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import org.instancio.Instancio;

import java.util.List;

public class TicketFactory {
    public static List<TicketEntity> createListTicketEntity(){
        return Instancio.ofList(TicketEntity.class).size(2).create();
    }
}
