package fdmapi.categoria.controller;

import java.util.Optional;

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

import fdmapi.categoria.dto.CategoriaDto;
import fdmapi.categoria.model.Categoria;
import fdmapi.categoria.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Tag(name = "fdmapi", description = "API para manter categoria.")
@Log4j2
@RequestMapping(path = "/fdm/categorias")
@RestController
public class CategoriaCtrl {

	private final CategoriaService categoriaService;

	public CategoriaCtrl(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@Operation(summary="Retornar todas as categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", 
				description=
					"Sucesso ao retornar todas as categorias"),
            @ApiResponse(responseCode="404", 
				description=
					"Falha ao encontrar categorias no banco de dados"),
            @ApiResponse(responseCode="500", 
				description=
					"Falha da Api ou da conexão com o servidor")
        }
    )
	@GetMapping
	public @ResponseBody Iterable<Categoria> getAll() {
		log.info("getAll()");
		return categoriaService.getAll();
	}

	@Operation(summary="Retornar uma categoria específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", 
				description=
					"Sucesso ao retornar a categoria"),
            @ApiResponse(responseCode="404", 
				description=
					"Falha ao encontrar a categoria no banco de dados"),
            @ApiResponse(responseCode="500", 
				description=
					"Falha da Api ou da conexão com o servidor")
        }
    )
	@GetMapping("/{id}")
    public @ResponseBody Optional<Categoria> getCategoria(@PathVariable Long id, HttpServletResponse response) {
        try {
            Optional<Categoria>categoria = categoriaService.findById(id);
            if (categoria == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
            return categoria;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
    }

	@Operation(summary="Inserir uma nova categoria no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", 
				description=
					"Sucesso ao inserir a nova categoria"),
            @ApiResponse(responseCode="400", 
				description=
					"Falha ao inserir a categoria no banco de dados, verifique a requisição")
        }
    )
	@PostMapping
	public ResponseEntity<CategoriaDto> create(@Valid @RequestBody CategoriaDto categoriaDto) {
		log.info("create( " + categoriaDto + " )");

		try {
			Categoria categoria = new Categoria();
			categoria.setNome(categoriaDto.nome());
			categoria.setAtivo(categoriaDto.ativo());

			Categoria categoriaSalva = categoriaService.save(categoria);

			return new ResponseEntity<>(CategoriaDto.from(categoriaSalva), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary="Atualizar uma categoria no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", 
				description=
					"Sucesso ao atualizar a categoria"),
            @ApiResponse(responseCode="400", 
				description=
					"Falha ao atualizar a categoria no banco de dados, verifique a requisição")
        }
    )
	@PutMapping("/{id}")
	public ResponseEntity<String> update(
		@Valid @RequestBody CategoriaDto categoriaDto, 
			@PathVariable Long id
	) {
		log.info("update( " + categoriaDto + ", Id " + id + " )");

		Optional<Categoria> categoriaData = categoriaService.findById(id);

		if (categoriaData.isPresent()) {
			Categoria categoria = categoriaData.get();
			categoria.setId(id);
			categoria.setNome(categoriaDto.nome());
			categoria.setAtivo(categoriaDto.ativo());

			categoriaService.save(categoria);

			return new ResponseEntity<>(
				"Categoria alterada com sucesso!", 
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
				"Não foi possível encontrar a Categoria.", 
					HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary="Remover uma categoria do banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", 
				description="Sucesso ao remover a categoria"),
            @ApiResponse(responseCode="400", 
				description=
					"Falha ao remover categoria do banco de dados, verifique a requisição")
        }
    )
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		log.info("delete( Id " + id + " )");

		try {
			categoriaService.deleteById(id);
			return new ResponseEntity<>("Categoria excluído com sucesso!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Não foi possível excluir a Categoria.",
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
