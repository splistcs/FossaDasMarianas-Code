package fdmapi.cadastro.dto;

import fdmapi.cadastro.model.Cadastro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de ENTRADA do Manter Cadastro (UC03).
 * Respostas usam CadastroRespostaDto, que não contém a senha.
 */
public record CadastroDto (

    Long id,

    @NotBlank(message = "Nome Inválido") String nomeCompleto,

    @NotBlank(message = "Email Inválido") String email,

    @NotBlank(message = "CPF Inválido") String CPF,

    @NotBlank(message = "Número de telefone inválido") String telefone,

    @NotNull(message = "Estado de atuação não está informado") Boolean ativo,

    @NotBlank(message = "Senha inválida") String senha,

    
    @NotNull(message = "Tipo de cadastro não está informado") Cadastro.TipoCadastro TipoCadastro
) {
    
    @Override
    public String toString() {
        return "CadastroDto[id=" + id + ", nomeCompleto=" + nomeCompleto + ", email=" + email
            + ", CPF=" + CPF + ", telefone=" + telefone + ", ativo=" + ativo
            + ", senha=*****, TipoCadastro=" + TipoCadastro + "]";
    }
}