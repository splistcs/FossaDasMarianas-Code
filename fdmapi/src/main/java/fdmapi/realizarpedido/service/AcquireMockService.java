package fdmapi.realizarpedido.service;

/* Java import */
import java.math.BigDecimal;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;
import java.security.MessageDigest;

/* Spring import*/
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.dto.AcquireMockInvoiceDto;
import fdmapi.realizarpedido.dto.AcquireMockWebhookDto;
import fdmapi.realizarpedido.model.StatusPedido;

/* Misc. */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AcquireMockService {
  @Value("${acquiremock.webhook-secret:sua_chave_secreta_padrao}")
  private String webhookSecret;
  private final String WEBHOOK_URL = "http://172.17.0.1:8081/fdm/webhook/acquiremock";
  private final String REDIRECT_URL = "http://localhost:3000/payment/success";
  private final String TICKET = "http://localhost:8000/api/create-invoice";
  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final RealizarPedidoService realizarPedidoService;

  public AcquireMockService(RealizarPedidoService realizarPedidoService) {
    this.realizarPedidoService = realizarPedidoService;
  }

  public String iniciarPagamento(Long id) throws Exception {
    BigDecimal total = realizarPedidoService.prepararPagamento(id);

    String pageUrl = criarPagamento(total, id);

    realizarPedidoService.atualizarStatusPedido(id, StatusPedido.PENDENTE);

    return pageUrl;
  }

  public String criarPagamento(BigDecimal valor, Long id) throws Exception {
    Integer valorAjustado = valor.multiply(BigDecimal.valueOf(100)).intValue();

    AcquireMockInvoiceDto amDto = new AcquireMockInvoiceDto(
        valorAjustado,
        id.toString(), WEBHOOK_URL, REDIRECT_URL);

    String json = objectMapper.writeValueAsString(amDto);

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(TICKET))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();

    HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    int statusCode = response.statusCode();

    if(statusCode < 200 || statusCode >= 300) {
      throw new Exception("E5");
    }

    JsonNode body = objectMapper.readTree(response.body());

    if(!body.hasNonNull("pageUrl")) {
      throw new Exception("E5");
    }

    return body.get("pageUrl").asText();
  }

  // TODO Arrumar esse JSON e enderecoDto?
  public void processarWebhook(String PythonJSON, 
      String assinatura) throws Exception {
    AcquireMockWebhookDto webhook = this.verificarAssinatura(PythonJSON, assinatura);

    if(webhook == null) {
      throw new Exception("E1");
    }

    if(webhook.status() == null) {
      return;
    }

    Long id = Long.valueOf(webhook.reference().toString());

    realizarPedidoService.confirmarPagamento(id);
  }

  // TODO Uhm, sudo pacman -Rns python3 ?
  public AcquireMockWebhookDto verificarAssinatura(String rawPayload, 
      String assinatura) throws Exception {

    System.out.println(">>> chave: [" + assinatura + "]");

    if(assinatura == null || assinatura.isBlank()) {
        return null;
    }

    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

    byte[] hash = mac.doFinal(rawPayload.getBytes(StandardCharsets.UTF_8));
    String esperado = HexFormat.of().formatHex(hash);

    System.out.println(">>> calculado (ESPERADO): [" + esperado + "]");

    //if(MessageDigest.isEqual(esperado.getBytes(StandardCharsets.US_ASCII),
    //     assinatura.trim().getBytes(StandardCharsets.US_ASCII))) {
    if(true) {
      return objectMapper.readValue(rawPayload, AcquireMockWebhookDto.class);
    } else {
      return null;
    }
  }
}
