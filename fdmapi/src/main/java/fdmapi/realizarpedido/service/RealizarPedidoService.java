package fdmapi.realizarpedido.service;

/* Java import */
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import fdmapi.realizarpedido.service.AcquireMockService;
import fdmapi.realizarpedido.dto.AcquireMockWebhookDto;

import fdmapi.mantercadastro.model.Cliente;
import fdmapi.mantercadastro.dto.ClienteDto;
import fdmapi.mantercadastro.dto.EnderecoDto;
import fdmapi.mantercadastro.service.ManterCadastroService;

import fdmapi.produto.model.Sku;
import fdmapi.produto.dto.SkuDto;
import fdmapi.produto.service.SkuService;

/**
 * RealizarPedidoService
 *
 * obs. E1: param inválidos  == BAD_REQUEST (400)
 *      E2: sem permissão    == UNAUTHORIZED (401)
 *      E3: erros no db      == NOT_FOUND (404)
 *      E4: error de negócio == ?
 * @author Magocho
 */

@Service
public class RealizarPedidoService {
	private final PedidoRepository pedidoRepository;
	private final ItemRepository itemRepository;
	private final SkuService skuService;
	private final ManterCadastroService manterCadastroService;
  private final AcquireMockService acquireMockService;

	public RealizarPedidoService(PedidoRepository pedidoRepository,
      ItemRepository itemRepository, SkuService skuService,
      ManterCadastroService manterCadastroService, 
      AcquireMockService acquireMockService) {

		this.pedidoRepository = pedidoRepository;
		this.itemRepository = itemRepository;
    this.skuService = skuService;
    this.manterCadastroService = manterCadastroService;
    this.acquireMockService = acquireMockService;
  }

  /* OK */
  public List<ItemDto> getAllItemPorPedido(Long pedidoId) throws Exception {
    if(pedidoId == null || pedidoId < 0) {
      throw new Exception("E1");
    }

    List<Item> resultado = itemRepository.selectPorPedidoDto(pedidoId);

    if(resultado.isEmpty()) {
      throw new Exception("E3");
    }

    List<ItemDto> itemDtos = new ArrayList<>();
    for(Item i : resultado) {
      itemDtos.add(ItemDto.from(i));
    }

    return itemDtos;
  }

  /* OK ? */
  public ItemDto poeCarrinhoItem(Long clienteId, Long skuId, Integer quant) throws Exception {
    if(quant == null || quant <= 0) {
      throw new Exception("E1");
    }

    if(!manterCadastroService.confirmaClientePorId(clienteId)) {
      throw new Exception("E2");
    }

    Pedido carrinho = pedidoRepository.selectPorClienteIdEStatusPedido(clienteId, StatusPedido.CARRINHO);

    if(carrinho == null) {
      Cliente cliente = manterCadastroService.findClienteById(clienteId).orElse(null);
      if(cliente == null) {
        throw new Exception("E3");
      }

      carrinho = new Pedido();
      carrinho.setStatusPedido(StatusPedido.CARRINHO);

      if(cliente.getPedidos() == null) {
        cliente.setPedidos(new ArrayList<>());
      }

      cliente.getPedidos().add(carrinho);
      manterCadastroService.save(cliente);

      carrinho = pedidoRepository.selectPorClienteIdEStatusPedido(clienteId, StatusPedido.CARRINHO);
    }

    Sku sku = skuService.findById(skuId).orElse(null);

    if(sku == null || sku.getPreco() == null 
        || sku.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
      throw new Exception("E1");
    }

    Item i = new Item();
    i.setQuantidade(quant);
    i.setSku(sku);
    i.setPrecoFinal(sku.getPreco().multiply(BigDecimal.valueOf(quant)));

    if(carrinho.getItemPedido() == null) {
      carrinho.setItemPedido(new ArrayList<>());
    }
    carrinho.getItemPedido().add(i);

    Item itemSalvo = itemRepository.save(i);
    pedidoRepository.save(carrinho);

    return ItemDto.from(itemSalvo);
  }

  /* OK */
  public ItemDto alterarItemCarrinho(Integer quant, Long id) throws Exception {
    if(quant == null || quant <= 0 || id < 0) {
      throw new Exception("E1");
    }

    Item i = itemRepository.findById(id).orElse(null);

    if(i == null) {
      throw new Exception("E3");
    }

    i.setQuantidade(quant);
    i.setPrecoFinal(i.getSku().getPreco().multiply(BigDecimal.valueOf(quant)));

    return ItemDto.from(i);
  }

  /* OK */
  public void deletarItemPorId(Long id) throws Exception {
		try {
      Item i = itemRepository.findById(id).orElse(null);

			if(i == null) {
				throw new Exception("Item inexistente.");
			}

      Integer estoque = i.getSku().getEstoque();
      estoque += i.getQuantidade();

      itemRepository.deleteById(id);
		} catch (Exception e) {
      throw e;
		}
	}

  /* OK */
  public PedidoDto getCarrinho(Long id) throws Exception {
    if(id == null || id < 0) {
      throw new Exception("E1");
    }

    Pedido carrinho = pedidoRepository.selectPorClienteIdEStatusPedido(id, StatusPedido.CARRINHO);

    if(carrinho == null) {
      Cliente cliente = manterCadastroService.findClienteById(id).orElse(null);

      if(cliente == null) {
        throw new Exception("E3");
      }

      carrinho = new Pedido();
      carrinho.setStatusPedido(StatusPedido.CARRINHO);
      carrinho.setItemPedido(new ArrayList<>());
      carrinho.setEndereco(null);
      carrinho = pedidoRepository.save(carrinho);

      if(cliente.getPedidos() == null) {
        cliente.setPedidos(new ArrayList<>());
      }

      cliente.getPedidos().add(carrinho);
      cliente = manterCadastroService.save(cliente);
    }

    return PedidoDto.from(carrinho);
	}

  /* OK */
  public PedidoDto getPedidoDtoPorId(Long id) {
    return PedidoDto.from(pedidoRepository.findById(id).orElse(null));
  }

  /* OK */
  public List<PedidoDto> getAllPedidoDtoPorClienteId(Long id) throws Exception {    
    if(id == null || id < 0) {
      throw new Exception("E1");
    }

    Cliente cliente = manterCadastroService.findClienteById(id).orElse(null);

    if(cliente == null) {
      throw new Exception("E3");
    }

    List<PedidoDto> pedidoDtos = new ArrayList<>();
    for(Pedido p : cliente.getPedidos()) {
      pedidoDtos.add(PedidoDto.from(p));
    }

    return pedidoDtos;
  }

  /* OK */
  public void deletarPedidoPorId(Long id) throws Exception {
    Pedido p = pedidoRepository.findById(id).orElse(null);

		if(p == null) {
			throw new Exception("E3");
		}
    pedidoRepository.deleteById(id);
	}

  /* OK */
  public Pedido findPedidoPorId(Long id) {
    return pedidoRepository.findById(id).orElse(null);
  }

  // TODO implementar CANCELADO
  public PedidoDto atualizarPedidoPorId(Long id) {
    Pedido p = pedidoRepository.findById(id).orElse(null);

    switch (p.getStatusPedido()) {
      case StatusPedido.CARRINHO:
      case StatusPedido.PENDENTE:
        break;
      case StatusPedido.PAGO:
        p.setStatusPedido(StatusPedido.ENVIADO);
        break;
      case StatusPedido.ENVIADO:
        p.setStatusPedido(StatusPedido.ENTREGUE);
        break;
      case StatusPedido.ENTREGUE:
        break;
      /* Não deve acontecer */
      case StatusPedido.CANCELADO:
        break;
    }
    pedidoRepository.save(p);

    return PedidoDto.from(p);
	}

  // TODO alterar os sku
  public String iniciarPagamento(Long id) throws Exception {
    Pedido p = pedidoRepository.findById(id).orElse(null);

    if(p == null) {
      throw new Exception("E3");
    }

    if(p.getStatusPedido() != StatusPedido.CARRINHO) {
        throw new Exception("E1");
    }

    BigDecimal total = BigDecimal.ZERO;
    if(p.getItemPedido() != null && !p.getItemPedido().isEmpty()) {
      for(Item item : p.getItemPedido()) {
        if(item.getPrecoFinal() != null) {
          total = total.add(item.getPrecoFinal());
        }
      }
    }

    if(total.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("E1");
    }

    p.setValorTotal(total);
    pedidoRepository.save(p);

    String pageUrl = acquireMockService.criarPagamento(
        p.getValorTotal(), p.getId());

    p.setStatusPedido(StatusPedido.PENDENTE);
    pedidoRepository.save(p);

    return pageUrl;
  }  

  // TODO Arrumar esse JSON e enderecoDto?
  public void processarWebhook(String PythonJSON, 
      String assinatura) throws Exception {
    AcquireMockWebhookDto webhook = acquireMockService.verificarAssinatura(PythonJSON, assinatura);

    if(webhook == null) {
      throw new Exception("E1");
    }

    if(webhook.status() == null) {
      return;
    }

    Integer id = Integer.valueOf(webhook.reference().toString());

    Pedido p = this.findPedidoPorId(new Long(id));

    if(p == null) {
      throw new Exception("E3");
    }

    if(p.getStatusPedido() == StatusPedido.PENDENTE) {
      p.setDataPedido(LocalDateTime.now());
      p.setStatusPedido(StatusPedido.PAGO);
      pedidoRepository.save(p);
    }
  }
}
