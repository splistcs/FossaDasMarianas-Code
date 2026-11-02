package fdmapi.realizarpedido.dto;

import java.math.BigDecimal;

/** 
 * AcquireMockInvoiceDto
 * Dto para a requesição de pagamento.
 *
 * @see url https://github.com/ashfromsky/acquiremock
 *
 * exemplo de curl:
 *
 * curl -X POST http://localhost:8000/api/create-invoice \
 * -H "Content-Type: application/json" \
 * -d '{
 *   "amount": 25000,
 *   "reference": "ORDER-123",
 *   "webhookUrl": "https://your-site.com/webhook",
 *   "redirectUrl": "https://your-site.com/success"
 * }'
 */

public record AcquireMockInvoiceDto(Integer amount, String reference, String webhookUrl, String redirectUrl) {

}
