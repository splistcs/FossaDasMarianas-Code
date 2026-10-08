package fdmapi.realizarpedido.dto;

/* Java import */
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.StatusPedido;
import fdmapi.realizarpedido.model.Item;
import fdmapi.realizarpedido.ItemDto;
import fdmapi.mantercadastro.model.Endereco;
import fdmapi.mantercadastro.dto.EnderecoDto;

/**
 * PedidoDto
 * Servir como transporte para Class Pedido,
 * isolando o db.
 *
 * @see fdmapi.realizarpedido.model.StatusPedido
 * @see fdmapi.realizarpedido.dto.ItemDto
 *
 * @author Magocho
 */

public record PedidoDto(Long id, LocalDateTime dataPedido, 
    BigDecimal valorTotal, BigDecimal valorFrete, Integer idSessao, 
    StatusPedido statusPedido, List<ItemDto> itemPedido, EnderecoDto enderecoDto) {

  public static PedidoDto fromSemItemSemEndereco (Pedido p){    
    return new PedidoDto(p.getId(), p.getDataPedido(), 
        p.getValorToral(), p.getValorFrete(), p.getIdSessao(),
        p.getStatusPedido(), null, null);
  }

  public static PedidoDto from (Pedido p){    
    List<ItemDto> itemDtos = new ArrayList<>();

    for(Item i : p.getItemPedido()) {
      itemDtos.add(ItemDto.from(i));
    }

    return new PedidoDto(p.getId(), p.getDataPedido(), 
        p.getValorTotal(), p.getValorFrete(), p.getIdSessao(),
        p.getStatusPedido(), itemDtos, EnderecoDto.from(p.getEndereco());
  }
}
