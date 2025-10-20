package com.tcc.api_ticket_sales.interfaces.dto.webhook.mercadopago;


import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//{
//        "id": 12345,
//        "live_mode": true,
//        "type": "payment",
//        "date_created": "2015-03-25T10:04:58.396-04:00",
//        "user_id": 44444,
//        "api_version": "v1",
//        "action": "payment.created",
//        "data": {
//        "id": "999999999"
//        }
//        }

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MercadoPagoWebhoookRequestDTO {
    @NotBlank
    private String id;

    @JsonProperty("live_mode")
    private boolean liveMode;

    @NotBlank
    private String type;

    @JsonProperty("date_created")
    @NotNull
    private String dateCreated;

    @JsonProperty("user_id")
    @NotNull
    private long userId;

    @JsonProperty("api_version")
    @NotBlank
    private String apiVersion;

    @NotBlank
    private String action;

    @NotNull
    @Valid
    private MercadoPagoWebhoookDataRequestDTO data;
}
