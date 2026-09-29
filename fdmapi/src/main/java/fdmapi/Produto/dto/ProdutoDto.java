package fdmapi.Produto.dto;

import java.util.List;

import fdmapi.Produto.model.Categoria;
import fdmapi.Produto.model.Produto;
import fdmapi.Produto.model.Sku;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoDto(

    Long id,

    @NotBlank(message = "nome inválido") String nome,

    @NotBlank(message = "descricao inválida") String descricao,

    @NotBlank(message = "material inválido") String material,

    @NotBlank(message = "marca inválida") String marca,

    @NotNull(message = "campo ativo inválido") Boolean ativo,

    String imagemPrincipalUrl,

    List<Sku> skus,

    Categoria categoria

) {
    public static ProdutoDto from (Produto produto){
        return new ProdutoDto(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getMaterial(), produto.getMarca(), produto.getAtivo(), produto.getImagemPrincipalUrl(), produto.getSkus(), produto.getCategoria());
    }
}
