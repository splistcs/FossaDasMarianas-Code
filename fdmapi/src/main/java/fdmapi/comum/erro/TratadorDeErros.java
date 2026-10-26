package fdmapi.comum.erro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

/**
 * Tratamento centralizado de erros: converte exceções em respostas HTTP
 * no formato padrão ProblemDetail (RFC 9457), em vez de try/catch em cada
 * controller.
 *
 * O escopo está limitado aos pacotes dos casos de uso novos, para não alterar
 * o comportamento dos controllers existentes. Novos pacotes devem ser
 * acrescentados à lista.
 */
@Log4j2
@RestControllerAdvice(basePackages = { "fdmapi.solicitarcadastro" })
public class TratadorDeErros {

    // 409: regra de negócio violada (ex.: e-mail já cadastrado)
    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail tratarConflito(ConflitoException e) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problema.setTitle("Conflito");
        return problema;
    }

    // 400: falha nas anotações de validação (@NotBlank, @CPF, ...).
    // Devolve um mapa campo -> mensagem, ex.: {"cpf": "CPF inválido",
    // "enderecos[0].cep": "CEP deve ter 8 dígitos"}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException e) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        e.getBindingResult().getGlobalErrors()
            .forEach(erro -> erros.putIfAbsent(erro.getObjectName(), erro.getDefaultMessage()));

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "Um ou mais campos são inválidos.");
        problema.setTitle("Dados inválidos");
        problema.setProperty("erros", erros);
        return problema;
    }

    // 400: corpo da requisição ausente ou JSON malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail tratarCorpoIlegivel(HttpMessageNotReadableException e) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "O corpo da requisição está ausente ou não é um JSON válido.");
        problema.setTitle("Requisição malformada");
        return problema;
    }

    // 409: restrição do banco violada (ex.: UNIQUE). Acontece quando duas
    // requisições simultâneas passam juntas pela verificação do service.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarIntegridade(DataIntegrityViolationException e) {
        log.warn("Violação de integridade: " + e.getMostSpecificCause().getMessage());
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
            "Os dados conflitam com um registro existente.");
        problema.setTitle("Conflito");
        return problema;
    }
}
