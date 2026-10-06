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
public class PesquisarProdutoCtrl {

}
