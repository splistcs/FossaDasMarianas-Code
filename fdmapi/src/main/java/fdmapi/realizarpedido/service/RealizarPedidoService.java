package fdmapi.realizarpedido.service;

/* Java import */
import java.util.Optional;

/* Spring import*/
import org.springframework.stereotype.Service;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.Item;
import fdmapi.realizarpedido.model.StatusPedido;
import fdmapi.realizarpedido.repository.ItemRepository;
import fdmapi.realizarpedido.repository.PedidoRepository;

@Service
public class RealizarPedidoService {
	private final PedidoRepository pedidoRepository;
	private final ItemRepository itemRepository;
  /*
	private final SkuService skuService;
	private final ClienteService clienteService;
	private final EnderecoService enderecoService;
  */

	public RealizarPedidoService(PedidoRepository pedidoRepository,
                               ItemRepository itemRepository) {
		this.pedidoRepository = pedidoRepository;
		this.itemRepository = itemRepository;
	}
}
