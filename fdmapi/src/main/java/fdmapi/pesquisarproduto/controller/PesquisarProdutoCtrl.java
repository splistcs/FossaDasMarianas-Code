package fdmapi.pesquisarproduto.controller;

/* Spring import */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* FossaDasMarinanas fdmapi import */
import fdmapi.produto.dto.ProdutoDto;
import fdmapi.pesquisarproduto.service.PesquisarProdutoService;

/* Misc. import */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * PesquisarProdutoCtrl
 * Objetivo é fornecer a API para a execução de uma barra de busca de Produto.
 * @author Magocho
 */

@Tag(name = "PesquisarProduto", description = "API para pesquisar produto.")
@RequestMapping(path = "/fdm")
@RestController
public class PesquisarProdutoCtrl {
	private final PesquisarProdutoService pesquisarProdutoService;

	public PesquisarProdutoCtrl(PesquisarProdutoService pesquisarProdutoService) {
		this.pesquisarProdutoService = pesquisarProdutoService;
	}

  /**
   *  Função retorna ProdutoDto que mais se aproxima
   *  dos parâmetros fornecidos e esteja ativo.
   *
   *  @param id  id da categoria.
   *  @see PesquisarProdutoService
   *
   *  03/10 - DISCUTIR NA PROX. PRINT:
   *  [1]. opção de limitar tamanho do retorno
   *  [2]. utilização do Long para categoria.
   * */
	@Operation(summary="Retornar um subconjunto de produtos específico", description = "Pesquisa produtos de acordo com os critérios informados.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
      @ApiResponse(responseCode = "400", description = "Nome não deve se ter nome"),
      @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
	@GetMapping("/pesquisarproduto")
  public ResponseEntity<Iterable<ProdutoDto>> getProdutos(
      @RequestParam(required = true) String nome,
      @RequestParam(required = false) String material,
      @RequestParam(required = false) String marca,
      @RequestParam(required = false) Long id) {
    try {
      ProdutoDto produtoDto = new ProdutoDto(null, nome, null, material, marca, null, null, null, null);
      Long categoriaId = id;

      Iterable<ProdutoDto> produtoDtos = pesquisarProdutoService.findByDto(produtoDto, id);

      if(produtoDtos.iterator().hasNext()) {
        return new ResponseEntity<>(produtoDtos, HttpStatus.OK);
      }

      produtoDtos = pesquisarProdutoService.findByDtoProximo(produtoDto, id);

      if(produtoDtos.iterator().hasNext()) {
        return new ResponseEntity<>(produtoDtos, HttpStatus.OK);
      }

      produtoDtos = pesquisarProdutoService.findByDtoSugestao(produtoDto, id);

      if(produtoDtos.iterator().hasNext()) {
        return new ResponseEntity<>(produtoDtos, HttpStatus.OK);
      }

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
