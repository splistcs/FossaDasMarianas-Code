package fdmapi.endereco.dto;

import fdmapi.endereco.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDto(

    Long id,

    @NotBlank(message = "CEP não informado")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve ter 8 dígitos")
    String cep,

    @NotBlank(message = "Número não informado") String numero,

    String complemento,

    @NotBlank(message = "Bairro não informado") String bairro,

    @NotBlank(message = "Rua não informada") String rua,

    @NotBlank(message = "Cidade não informada") String cidade
) {
    public static EnderecoDto from(Endereco endereco) {
        return new EnderecoDto(endereco.getId(), endereco.getCep(), endereco.getNumero(),
            endereco.getComplemento(), endereco.getBairro(), endereco.getRua(), endereco.getCidade());
    }
}
