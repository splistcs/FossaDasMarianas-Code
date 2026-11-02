package fdmapi.cadastro.dto;

import java.util.List;

import fdmapi.cadastro.model.Cadastro;
import fdmapi.endereco.dto.EnderecoDto;

/**
 * DTO de SAÍDA de cadastro: tudo o que a API pode devolver sobre um cadastro.
 * Não contém a senha
 */
public record CadastroRespostaDto(
    Long id,
    String nomeCompleto,
    String email,
    String cpf,
    String telefone,
    boolean ativo,
    Cadastro.TipoCadastro tipoCadastro,
    List<EnderecoDto> enderecos
) {
    /**
     * Atenção: lê a lista de endereços, que é carregada sob demanda (LAZY).
     * Com open-in-view=false, chame este método dentro de uma transação
     * (no service) ou com uma entidade recém-criada.
     */
    public static CadastroRespostaDto from(Cadastro cadastro) {
        return new CadastroRespostaDto(
            cadastro.getId(),
            cadastro.getNomeCompleto(),
            cadastro.getEmail(),
            cadastro.getCpf(),
            cadastro.getTelefone(),
            cadastro.isAtivo(),
            cadastro.getTipoCadastro(),
            cadastro.getEnderecos().stream().map(EnderecoDto::from).toList()
        );
    }
}
