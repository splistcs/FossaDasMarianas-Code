package FDM.api.ctrl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import FDM.api.dto.ProdutoDto;
import FDM.api.model.Produto;
import FDM.api.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Controller
@Tag(name = "FDM-api", description= "Api FDM")
@Log4j2
@RequestMapping("/fdm/produtos")
public class ProdutoCtrl {

    @Autowired
    private ProdutoRepository repository;

    @Operation(summary="Retornar todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao retornar todos os produtos"),
            @ApiResponse(responseCode="404", description="Falha ao encontrar produtos no banco de dados"),
            @ApiResponse(responseCode="500", description="Falha da Api ou da conexão com o servidor")
        }
    )
    @GetMapping
    public @ResponseBody Iterable<Produto> getProdutos(HttpServletResponse response) {
        try {
            Iterable<Produto> produtos = repository.findAll();
            if (produtos == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
            return produtos;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
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
            Optional<Produto> produto = repository.findById(id);
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
    public ResponseEntity<Void> postProduto (@RequestBody @Validated ProdutoDto request){
        try {
           Produto produto = new Produto();
            produto.setNome(request.nome());
            produto.setDescricao(request.descricao());
            produto.setMaterial(request.material());
            produto.setMarca(request.marca());
            produto.setAtivo(request.ativo());
            produto.setImagemPrincipalUrl(request.imagemPrincipalUrl());
            produto.setSkus(request.skus());
            produto.setCategoria(request.categoria());

            repository.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Falha ao inserir produto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary="Atualizar um produto no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao atualizar o produto"),
            @ApiResponse(responseCode="400", description="Falha ao atualizar produto no banco de dados, verifique a requisição")
        }
    )
    @PutMapping("/{id}")
    public @ResponseBody HttpStatus putProduto (@RequestBody @Validated ProdutoDto request, @PathVariable Long id){
        try {
            Produto produto = new Produto();
            produto.setId(id);
            produto.setNome(request.nome());
            produto.setDescricao(request.descricao());
            produto.setMaterial(request.material());
            produto.setMarca(request.marca());
            produto.setAtivo(request.ativo());
            produto.setImagemPrincipalUrl(request.imagemPrincipalUrl());
            produto.setSkus(request.skus());
            produto.setCategoria(request.categoria());
            
            repository.save(produto);

            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
        

    }

    @Operation(summary="Remover um produto do banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Sucesso ao remover o produto"),
            @ApiResponse(responseCode="400", description="Falha ao remover produto do banco de dados, verifique a requisição")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Falha ao remover produto id=" + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
