package fdmapi.comum.erro;

/**
 * Violação de regra de negócio que conflita com o estado atual dos dados
 * (ex.: e-mail já cadastrado). É convertida em HTTP 409 pelo TratadorDeErros.
 */
public class ConflitoException extends RuntimeException {

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
