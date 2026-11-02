package fdmapi.solicitarcadastro.dto;

import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import fdmapi.endereco.dto.EnderecoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA do UC01 (Solicitar Cadastro).
 *
 * Não tem id, ativo nem tipoCadastro: esses valores são definidos pelo
 * servidor (CLIENTE, inativo até a aprovação do gestor). Assim, quem se
 * cadastra não consegue se registrar como ADMIN nem já ativo.
 */
public record SolicitacaoCadastroDto(

    @NotBlank(message = "Nome não informado")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    String nomeCompleto,

    @NotBlank(message = "E-mail não informado")
    @Email(message = "E-mail inválido")
    String email,

    // Aceita "123.456.789-09" ou "12345678909" e confere os dígitos verificadores
    @NotBlank(message = "CPF não informado")
    @CPF(message = "CPF inválido")
    String cpf,

    // Aceita, por exemplo, "(11) 99999-9999", "11 99999 9999" ou "11999999999"
    @NotBlank(message = "Telefone não informado")
    @Pattern(regexp = "\\(?\\d{2}\\)?\\s?\\d{4,5}[-\\s]?\\d{4}",
             message = "Telefone deve ter DDD e 8 ou 9 dígitos")
    String telefone,

    // O BCrypt considera no máximo 72 bytes; acima disso a senha seria rejeitada
    @NotBlank(message = "Senha não informada")
    @Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
    String senha,

    // @NotEmpty: ao menos um endereço (multiplicidade 1..* do diagrama)
    // @Valid: valida também cada endereço da lista
    @NotEmpty(message = "Informe ao menos um endereço")
    List<@Valid EnderecoDto> enderecos
) {
    // O toString() padrão de um record imprimiria a senha no log
    @Override
    public String toString() {
        return "SolicitacaoCadastroDto[nomeCompleto=" + nomeCompleto + ", email=" + email
            + ", cpf=" + cpf + ", telefone=" + telefone + ", senha=*****, enderecos=" + enderecos + "]";
    }
}
