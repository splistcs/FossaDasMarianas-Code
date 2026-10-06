package fdmapi.realizarpedido.dto;

/* Java import */
import java.math.BigDecimal;

/* FossaDasMarinanas fdmapi import */
import fdmapi.produto.model.Sku;
//import fdmapi.produto.dto.SkuDto;
import fdmapi.realizarpedido.model.Item;

/**
 * ItemDto
 * Servir como transporte para Class Item,
 * isolando o db, contém opção para formar
 * de um Item completo, com um Sku e sem Sku.
 *
 * @see fdmapi.produto.model.Sku
 * @see fdmapi.produto.dto.SkuDto#from()
 *
 * @author Magocho
 */

public record ItemDto (Long id, Integer quantidade, BigDecimal precoFinal, SkuDto skuDto) {
  public static ItemDto from (Item i){
  //  return new ItemDto(i.getId(), i.getQuantidade(), i.getPrecoFinal(), SkuDto.from(i.getSku()));
  }

  public static ItemDto fromComSku (Item i, Sku sku){
  //  return new ItemDto(i.getId(), i.getQuantidade(), i.getPrecoFinal(), SkuDto.from(sku));
  }

  public static ItemDto fromSemSku (Item i){
    return new ItemDto(i.getId(), i.getQuantidade(), i.getPrecoFinal(), null);
  }
}
