package fdmapi.categoria.dto;

import fdmapi.categoria.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaDto(

    Long id,

    @NotBlank(message = "nome inválido") String nome,

    @NotNull(message = "campo ativo inválido") Boolean ativo

) {
    public static CategoriaDto from (Categoria categoria){
        return new CategoriaDto(categoria.getId(), 
            categoria.getNome(), categoria.getAtivo());
    }
}
