package fdmapi.produto.dto;

import java.math.BigDecimal;
import java.util.Map;

import fdmapi.produto.model.Sku;
import jakarta.validation.constraints.NotBlank;

public record SkuDto(

    Long id,

    Integer estoque,

    BigDecimal preco,

    Map<String, String> especificacoes,
    
    Integer pesoGramas,

    @NotBlank String codigoUniversal,

    Integer alturaCm,

    Integer larguraCm,

    Integer comprimentoCm

) {
    public static SkuDto from (Sku sku){
        return new SkuDto(sku.getId(), sku.getEstoque(), 
            sku.getPreco(), sku.getEspecificacoes(), 
                sku.getPesoGramas(), sku.getCodigoUniversal(), 
                    sku.getAlturaCm(), sku.getLarguraCm(), 
                        sku.getComprimentoCm());
    }
}
