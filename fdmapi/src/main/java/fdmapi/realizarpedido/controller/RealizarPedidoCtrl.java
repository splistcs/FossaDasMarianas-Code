package fdmapi.realizarpedido.controller;

/* Spring import */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* FossaDasMarinanas fdmapi import */

/* Misc. import */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * RealizarPedidoCtrl
 * Objetivo é fornecer a API para o ciclo de vida de um Pedido.
 * @author Magocho
 */

@Tag(name = "realizarpedido", description = "API para realizar pedido.")
@RequestMapping(path = "/fdm")
@RestController
public class RealizarPedidoCtrl {

  /**
   * [1] Ciclo de vida de Item
   */
	@Operation(summary = "Criar um item pelo id do Sku", description="retornar o id do item")
	@PostMapping(path = "/item")
	public ResponseEntity<Long> setItemSemSenha(@RequestParam Long clienteId, 
      @RequestParam Long skuId, @RequestParam Integer quant) {
  }

	@Operation(summary = "Criar um item pelo id do Sku", description="retornar o id do item")
	@PostMapping(path = "/item")
	public ResponseEntity<Long> setItemComSenha(
      @RequestParam Long clienteId, @RequestParam Long skuId, 
      @RequestParam String senha, @RequestParam Integer quant) {
  }

	@Operation(summary = "Atualizar a quant de um item.", description="retornar o itemDto")
	@PutMapping("/item/{id}")
	public ResponseEntity<ItemDto> update(@RequestParam Integer quant, @PathVariable Long id) {
	}
	

	@Operation(summary = "Exclui um item por Id.")
	@DeleteMapping("/item/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
	}
	

	@Operation(summary = "Recuperar item de Pedido.", description = "Uma coleção de item.")
	@GetMapping("/items/{id}")
	public ResponseEntity<Iterable<ItemDto>> getAllItem(@RequestParam Long pedidoId) {
	}


  /**
   * [2] Ciclo de vida de Pedido
   */
  @Operation(summary = "Fechar/Criar Pedido", description = "Converte os itens do carrinho em um Pedido com frete e endereço de entrega.")
  @PostMapping("/pedido")
  public ResponseEntity<PedidoDto> criarPedido(
      @RequestParam Long clienteId, @RequestParam Long enderecoId) {
        
        // Cria o pedido inicial com StatusPedido.PENDENTE ou CARRINHO conforme a regra
        PedidoDto pedido = new PedidoDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @Operation(summary = "Consultar Pedido por ID")
    @GetMapping("/pedido/{id}")
    public ResponseEntity<PedidoDto> getPedidoById(@PathVariable Long id) {
        PedidoDto pedido = new PedidoDto();
        return ResponseEntity.ok(pedido);
    }

    @Operation(summary = "Listar Pedidos de um Cliente")
    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoDto>> getPedidosByCliente(@RequestParam Long clienteId) {
        return ResponseEntity.ok(List.of());
    }

    @Operation(summary = "Atualizar status do pedido", description = "Transiciona o status (ex: PENDENTE -> PAGO -> ENVIADO -> ENTREGUE ou CANCELADO).")
    @PatchMapping("/pedido/{id}/status")
    public ResponseEntity<PedidoDto> alterarStatusPedido(
            @PathVariable Long id, 
            @RequestParam StatusPedido status) {
        
        // Atualiza o enum StatusPedido no banco através do RealizarPedidoService
        PedidoDto pedidoAtualizado = new PedidoDto();
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Cancelar um pedido por ID")
    @PostMapping("/pedido/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        // Altera o status do pedido para CANCELADO
        return ResponseEntity.noContent().build();
    }
}
