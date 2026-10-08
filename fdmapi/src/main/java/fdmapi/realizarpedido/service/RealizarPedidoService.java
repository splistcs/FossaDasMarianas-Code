package fdmapi.realizarpedido.service;

/* Java import */
import java.util.Optional;

/* Spring import*/
import org.springframework.stereotype.Service;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.Item;
import fdmapi.realizarpedido.model.StatusPedido;
import fdmapi.realizarpedido.dto.PedidoDto;
import fdmapi.realizarpedido.dto.ItemDto;
import fdmapi.realizarpedido.repository.ItemRepository;
import fdmapi.realizarpedido.repository.PedidoRepository;

package fdmapi.mantercadastro.dto.ClienteDto;
package fdmapi.mantercadastro.dto.EnderecoDto;
package fdmapi.mantercadastro.service.ManterCadastroService;

package fdmapi.produto.dto.SkuDto;
package fdmapi.produto.service.ProdutoService;

@Service
public class RealizarPedidoService {
	private final PedidoRepository pedidoRepository;
	private final ItemRepository itemRepository;
	private final ProdutoService produtoService;
	private final ManterCadastroService manterCadastroService;

	public RealizarPedidoService(PedidoRepository pedidoRepository,
                               ItemRepository itemRepository,
                               ProdutoService produtoService,
                               ManterCadastroService manterCadastroService) {
		this.pedidoRepository = pedidoRepository;
		this.itemRepository = itemRepository;
    this.produtoService = produtoService;
    this.manterCadastroService = manterCadastroService;
	}
}
