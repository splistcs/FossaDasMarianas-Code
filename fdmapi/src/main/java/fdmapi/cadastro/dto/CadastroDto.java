package fdmapi.cadastro.dto;

import fdmapi.cadastro.model.Cadastro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroDto (
    
    Long id,

    @NotBlank(message = "Nome Inválido") String nomeCompleto,

    @NotBlank(message = "Email Inválido") String email,

    @NotBlank(message = "CPF Inválido") String CPF,

    @NotBlank(message = "Número de telefone inválido") String telefone,

    @NotNull(message = "Estado de atuação não está informado") Boolean ativo,

    @NotBlank(message = "Senha inválida") String senha,

    @NotBlank(message = "Tipo de cadastro não está informado") Cadastro.TipoCadastro TipoCadastro
) {
    public static CadastroDto from (Cadastro cadastro){
        return new CadastroDto(cadastro.getId(), cadastro.getNomeCompleto(), cadastro.getEmail(),
            cadastro.getCpf(), cadastro.getTelefone(), cadastro.isAtivo(), cadastro.getSenha(), cadastro.getTipoCadastro());
    }
}
