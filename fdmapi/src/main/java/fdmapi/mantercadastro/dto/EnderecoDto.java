package fdmapi.mantercadastro.dto;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Endereco;

/**
 * EnderecoDto
 * Servir como transporte para Class Endereco,
 * isolando o db, o curioso é ser praticamente
 * autocontida.
 *
 * @see fdmapi.mantercadastro.model.Endereco
 *
 * @author Magocho
 */

public record EnderecoDto (Long id, String cep, String numero, 
    String complemento, String bairro, String rua, String cidade) {

  public static EnderecoDto from (Endereco e){
    return new EnderecoDto(e.getId(), e.getCep(), e.getNumero(),
        e.getComplemento(), e.getBairro(), e.getRua(), e.getCidade());
  }
}
