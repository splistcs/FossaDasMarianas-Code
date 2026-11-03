package fdmapi.realizarpedido.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AcquireMockWebhookDto(
    @JsonProperty("payment_id") String paymentId, 
    String reference, 
    Integer amount, 
    String status, 
    String timestamp,
    @JsonProperty("card_mask") String cardMask
    ) { }
