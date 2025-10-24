package com.tcc.api_ticket_sales.application.dto.webhook.mercadopago;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private MercadoPagoWebhookDataRequestDTO data;
}
