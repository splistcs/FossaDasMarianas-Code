package fdmapi.produto.dto;

/* Java import */
import java.math.BigDecimal;
import java.util.Map;

/* FossaDasMarinanas fdmapi import */
import fdmapi.produto.model.Sku;

/**
 * SkuDto
 * Servir como transporte para Class Sku,
 * isolando o db, o curioso é ser praticamente
 * autocontida como a Class Endereco.
 *
 * @see fdmapi.mantercadastro.model.Endereco
 * @see fdmapi.produto.model.Sku
 *
 * @author Magocho
 */

public record SkuDto(Long id, Integer estoque, BigDecimal preco, 
    Map<String, String> especificacoes, Integer pesoGramas, 
    String codigoUniversal, Integer alturaCm, Integer larguraCm, 
    Integer comprimentoCm) {

  public static SkuDto from (Sku s){    
    return new SkuDto(s.getId(), s.getEstoque(), s.getPreco(),
        s.getEspecificacoes(), s.getPesoGramas(), 
        s.getCodigoUniversal(), s.getAlturaCm(), s.getLarguraCm(), 
        s.getComprimentoCm());
  }
}
