package fdmapi.realizarpedido.controller;

/* Java import */
import java.util.List;

/* Spring import */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.service.RealizarPedidoService;
import fdmapi.realizarpedido.dto.ItemDto;
import fdmapi.realizarpedido.dto.PedidoDto;
import fdmapi.realizarpedido.model.StatusPedido;
import fdmapi.realizarpedido.dto.AcquireMockWebhookDto;

/* Misc. import */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * RealizarPedidoCtrl
 * Objetivo é fornecer a API para o ciclo de vida de um Pedido
 * e por consequência do Item também.
 *
 * @author Magocho
 */

@Tag(name = "realizarpedido", description = "API para realizar pedido.")
@RequestMapping(path = "/fdm")
@RestController
public class RealizarPedidoCtrl {
  private final RealizarPedidoService realizarPedidoService;

  public RealizarPedidoCtrl(RealizarPedidoService realizarPedidoService) {
    this.realizarPedidoService = realizarPedidoService;
  }

  /** [1] Ciclo de vida de Item */

  /* C - criar */
	@Operation(summary = "Criar um item pelo id do Sku", description="retornar o DTO do item")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "ItemDto criado."),
      @ApiResponse(responseCode = "400", description = "Erro na criação."),
      @ApiResponse(responseCode = "401", description = "Não tem permissão."),
      @ApiResponse(responseCode = "404", description = "Nenhum item encontrado."),
  })
	@PostMapping(path = "/item")
	public ResponseEntity<ItemDto> setItemSemSenha(@RequestParam Long clienteId,
      @RequestParam Long skuId, @RequestParam Integer quant) {

    try {
      ItemDto itemDto = realizarPedidoService.poeCarrinhoItem(clienteId, skuId, quant);

      return new ResponseEntity<>(itemDto, HttpStatus.OK);
    } catch (Exception e) {
      if("E2".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /* U - atualizar */
	@Operation(summary = "Atualizar a quant de um item.", description="retornar o itemDto")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "ItemDto criado."),
      @ApiResponse(responseCode = "400", description = "Erro na criação."),
      @ApiResponse(responseCode = "404", description = "Nenhum item encontrado."),
  })
	@PutMapping("/item/{id}")
	public ResponseEntity<ItemDto> atualizarItem(@RequestParam Integer quant, @PathVariable Long id) {
    try {
      ItemDto itemDto = realizarPedidoService.alterarItemCarrinho(quant, id);

      return new ResponseEntity<>(itemDto, HttpStatus.OK);
    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
	}

  /* D - Remover */
	@Operation(summary = "Exclui um item por Id.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Item removido."),
  })
	@DeleteMapping("/item/{id}")
	public ResponseEntity<Void> deletarItem(@PathVariable Long id) {
		try {
			realizarPedidoService.deletarItemPorId(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
  /* Talvez não seja necessário */
  /* R - ler */
	@Operation(summary = "Recuperar item de Pedido.", description = "Uma coleção de item.") 
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Item do pedido encontrado."),
      @ApiResponse(responseCode = "400", description = "Nenhum pedido encontrado."),
      @ApiResponse(responseCode = "404", description = "Nenhum item encontrado."),
  })
	@GetMapping("/items/{id}")
	public ResponseEntity<Iterable<ItemDto>> getAllItem(@RequestParam Long pedidoId) {
    try {
      List<ItemDto> items = realizarPedidoService.getAllItemPorPedido(pedidoId);

      return new ResponseEntity<>(items, HttpStatus.OK);
    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
	}

  /** [2] Ciclo de vida de Pedido */

  /* C - criar */
  @Operation(summary = "Criar Pedido", description = "Cria o pedido, todos começam como CARRINHO")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pedido criado!"),
      @ApiResponse(responseCode = "400", description = "Cliente não encontrado."),
      @ApiResponse(responseCode = "404", description = "Erros no db."),
  })
  @PostMapping("/pedido") 
  public ResponseEntity<PedidoDto> criarPedido(@RequestParam Long clienteId) {
    try {
      PedidoDto pedidoDto = realizarPedidoService.getCarrinho(clienteId);
      
      return new ResponseEntity<>(pedidoDto, HttpStatus.OK);
    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /* R - ler */
  @Operation(summary = "Consultar Pedido por Id", description = "Retorna o pedido")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pedido encontrado!"),
      @ApiResponse(responseCode = "404", description = "Erros no db."),
  })
  @GetMapping("/pedido/{id}")  
  public ResponseEntity<PedidoDto> getPedidoPorId(@PathVariable Long id) {
    PedidoDto pedido = realizarPedidoService.getPedidoDtoPorId(id);

    if(pedido == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(pedido, HttpStatus.OK);
  }

  /* R - ler todos */
  @Operation(summary = "Listar Pedidos de um Cliente", description = "Retorna a lista")  
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pedidos encontrados!"),
      @ApiResponse(responseCode = "400", description = "Cliente não encontrado."),
      @ApiResponse(responseCode = "404", description = "Erros no db."),
  })
  @GetMapping("/pedidos")
  public ResponseEntity<List<PedidoDto>> getPedidosPorCliente(@RequestParam Long clienteId) {
    try {
      List<PedidoDto> pedidoDtos = realizarPedidoService.getAllPedidoDtoPorClienteId(clienteId);
      
      return new ResponseEntity<>(pedidoDtos, HttpStatus.OK);
    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /** 
   * AVISO: USAR COM PARCIMÔNIA, PREFIRA:
   * @see alterarStatusPedido
   * */

  /* D - Remover */
  @Operation(summary = "Remover um pedido por Id", description = "Deleta o pedido")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pedido removido!"),
      @ApiResponse(responseCode = "404", description = "Erros no db."),
  })
  @DeleteMapping("/pedido/{id}/REMOVER")
  public ResponseEntity<Void> removerPedido(@PathVariable Long id) {
    try {
      realizarPedidoService.deletarPedidoPorId(id);
      
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /* U - Atualizar */
  // TODO CANCELAR
  // Ta ta ta ta ra rara papa (Charles)
  // "Derrubando o db"
  // sqlite3 NOT_FOUND
  @Operation(summary = "Atualizar status do pedido", description = "Transiciona o status (PAGO -> ENVIADO -> ENTREGUE ou CANCELADO).")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pedido atualizado!"),
      @ApiResponse(responseCode = "404", description = "Erros no db."),
  })
  @PutMapping("/pedido/{id}/status")
  public ResponseEntity<PedidoDto> alterarStatusPedido(
      @PathVariable Long id, @RequestParam StatusPedido status) {
    try {
      PedidoDto dto = null;
      if(status == StatusPedido.CANCELADO) {
      } else {
        dto = realizarPedidoService.atualizarPedidoPorId(id);
      }
      
      return new ResponseEntity<>(dto, HttpStatus.OK);
    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }   
  }

  // TODO colocar o endereco_id ?
  @Operation(summary = "Começa o pagamento", description = "Recebe a atualização de um pagamento")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Url retornada!"),
      @ApiResponse(responseCode = "400", description = "Não é CARRINHO"),
      @ApiResponse(responseCode = "403", description = "AcquireMock negou pedido."),
      @ApiResponse(responseCode = "404", description = "Pedido não encontrado db."),
  })
  @PostMapping("/pedido/{id}/pagamento")
  public ResponseEntity<String> iniciarPagamento(@PathVariable Long id) {
    try {
      String url = realizarPedidoService.iniciarPagamento(id);

      return new ResponseEntity<>(url, HttpStatus.OK);

    } catch (Exception e) {
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      } else if("E5".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // TODO apagar isso
  /* SR Littorio - curl Bombardamento! */
  /* Oh no! SUPER EFFECTIVE............*/
  /* R: -1 hp docker               */
  /* SSSSR Murakami: "Como funciona pagamento?" */
  /* MEGA CRITICAL, OVERKILL! - infinito */
  /* R: SR Littorio sunk! docker morto! Magocho sad effect... */
  @Operation(summary = "Receber webhook do AcquireMock", description = "Recebe a atualização de um pagamento")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Processado"),
      @ApiResponse(responseCode = "400", description = "Erros na assinatura."),
      @ApiResponse(responseCode = "404", description = "Pedido não encontrado db."),
  })
  @PostMapping("/webhook/acquiremock")
  public ResponseEntity<Void> receberWebhook(
      @RequestBody String PythonLOVE,
      @RequestHeader(value = "X-Signature", required = false) String assinatura) {

    try {
      realizarPedidoService.processarWebhook(PythonLOVE, assinatura);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      if("E3".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else if("E1".equals(e.getMessage())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
