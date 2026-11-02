package fdmapi.comum.erro;

/**
 * Recurso inexistente (ex.: id de solicitação que não existe).
 * É convertida em HTTP 404 pelo TratadorDeErros.
 */
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
