package fdmapi.realizarpedido.dto;

/* Java import */
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Item;
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.StatusPedido;
import fdmapi.realizarpedido.ItemDto;
//import fdmapi.<----->.Endereco;
//import fdmapi.<----->.EnderecoDto;

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

public record PedidoDto(Long id, LocalDateTime dataPedido, BigDecimal valorTotal, 
                        BigDecimal valorFrete, Integer idSessao, StatusPedido statusPedido, 
                        List<ItemDto> itemPedido) {
  /*, EnderecoDto enderecoDto) { */

  public static PedidoDto from (Pedido p){
    return new PedidoDto(p.getId(), p.getDataPedido(), p.getValorToral(),
                         p.getValorFrete(), p.getIdSessao(), p.getStatusPedido(), 
                         p.getItemPedido());
    /* , EnderecoDto.from(p.getEndereco());*/
  }
}
