package fdmapi.Produto.ctrl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fdmapi.Produto.dto.ProdutoDto;
import fdmapi.Produto.model.Produto;
import fdmapi.Produto.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Tag(name = "fdmapi", description = "API para manter produto.")
@Log4j2
@RequestMapping(path = "/fdm/produtos")
@RestController
public class ProdutoCtrl {

	@Autowired
	private ProdutoService produtoService;

	@Operation(summary="Retornar todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao retornar todos os produtos"),
            @ApiResponse(responseCode="404", description="Falha ao encontrar produtos no banco de dados"),
            @ApiResponse(responseCode="500", description="Falha da Api ou da conexão com o servidor")
        }
    )
	@GetMapping
	public @ResponseBody Iterable<Produto> getAll() {
		log.info("getAll()");
		return produtoService.getAll();
	}

	@Operation(summary="Retornar um produto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao retornar o produto"),
            @ApiResponse(responseCode="404", description="Falha ao encontrar produto no banco de dados"),
            @ApiResponse(responseCode="500", description="Falha da Api ou da conexão com o servidor")
        }
    )
	@GetMapping("/{id}")
    public @ResponseBody Optional<Produto> getProduto(@PathVariable Long id, HttpServletResponse response) {
        try {
            Optional<Produto> produto = produtoService.findById(id);
            if (produto == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
            return produto;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
    }

	@Operation(summary="Inserir um novo produto no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", description="Sucesso ao inserir o novo produtoo"),
            @ApiResponse(responseCode="400", description="Falha ao inserir produto no banco de dados, verifique a requisição")
        }
    )
	@PostMapping
	public ResponseEntity<ProdutoDto> create(@Valid @RequestBody ProdutoDto produtoDto) {
		log.info("create( " + produtoDto + " )");

		try {
			Produto produto = new Produto();
			produto.setNome(produtoDto.nome());
			produto.setDescricao(produtoDto.descricao());
			produto.setMaterial(produtoDto.material());
			produto.setMarca(produtoDto.marca());
			produto.setAtivo(produtoDto.ativo());
			produto.setImagemPrincipalUrl(produtoDto.imagemPrincipalUrl());
			produto.setSkus(produtoDto.skus());
			produto.setCategoria(produtoDto.categoria());

			Produto produtoSalvo = produtoService.save(produto);

			return new ResponseEntity<>(ProdutoDto.from(produtoSalvo), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary="Atualizar um produto no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao atualizar o produto"),
            @ApiResponse(responseCode="400", description="Falha ao atualizar produto no banco de dados, verifique a requisição")
        }
    )
	@PutMapping("/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody ProdutoDto produtoDto, @PathVariable Long id) {
		log.info("update( " + produtoDto + ", Id " + id + " )");

		Optional<Produto> produtoData = produtoService.findById(id);

		if (produtoData.isPresent()) {
			Produto produto = produtoData.get();
			produto.setId(id);
			produto.setNome(produtoDto.nome());
			produto.setDescricao(produtoDto.descricao());
			produto.setMaterial(produtoDto.material());
			produto.setMarca(produtoDto.marca());
			produto.setAtivo(produtoDto.ativo());
			produto.setImagemPrincipalUrl(produtoDto.imagemPrincipalUrl());
			produto.setSkus(produtoDto.skus());
			produto.setCategoria(produtoDto.categoria());

			produtoService.save(produto);

			return new ResponseEntity<>("Produto alterado com sucesso!", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Não foi possível encontrar o Produto.", HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary="Remover um produto do banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao remover o produto"),
            @ApiResponse(responseCode="400", description="Falha ao remover produto do banco de dados, verifique a requisição")
        }
    )
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		log.info("delete( Id " + id + " )");

		try {
			produtoService.deleteById(id);
			return new ResponseEntity<>("Produto excluído com sucesso!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Não foi possível excluir o Produto.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
