package fdmapi.cadastro.controller;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;

import fdmapi.cadastro.dto.CadastroDto;
import fdmapi.cadastro.model.Cadastro;
import fdmapi.cadastro.service.CadastroService;

@Tag(name = "fdmapi", description = "API para manter cadastros.")
@Log4j2
@RequestMapping(path = "/fdm/cadastros")
@RestController
public class CadastroCtrl {
    
    private final CadastroService cadastroService;

    public CadastroCtrl(CadastroService cadastroService) {
        this.cadastroService=cadastroService;
    }

    @Operation(summary="Retornar toos os cadastros")
    @ApiResponses(value = {
        @ApiResponse(responseCode="200", description = "Sucesso ao retornar todos os cadastros"),
        @ApiResponse(responseCode = "404", description = "Falha ao encontrar cadastros no banco de dados"),
        @ApiResponse(responseCode = "500", description = "Falha da API ou da conexão com o servidor")
        }
    )
    @GetMapping
    public @ResponseBody Iterable<Cadastro> getAll() {
        log.info("getAll()");
        return cadastroService.getAll();
    }

    @Operation(summary="Retornar um cadastro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao retornar o cadastro"),
        @ApiResponse(responseCode = "404", description = "Falha ao encontrar o cadastro no banco de dados"),
        @ApiResponse(responseCode = "500", description = "Falha da Api ou da conexão com o servidor")
        }
    )
    @GetMapping("/{id}")
    public @ResponseBody Optional<Cadastro> getCadastro(@PathVariable Long id, HttpServletResponse response) {
        try {
            Optional<Cadastro>cadastro = cadastroService.findById(id);
            if (cadastro == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
            return cadastro;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
    }

    @Operation(summary = "Inserir um novo cadastro no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso ao inserir o novo cadastro"),
        @ApiResponse(responseCode = "400", description = "Falha ao inserir o cadastro no banco de dados, verifique a requisição")
        }
    )
    @PostMapping
    public ResponseEntity<CadastroDto> create(@Valid @RequestBody CadastroDto cadastroDto) {
        log.info("create (" + cadastroDto + " )");

        try {
            Cadastro cadastro = new Cadastro();
            cadastro.setNomeCompleto(cadastroDto.nomeCompleto());
            cadastro.setEmail(cadastroDto.email());
            cadastro.setCpf(cadastroDto.CPF());
            cadastro.setTelefone(cadastroDto.telefone());
            cadastro.setAtivo(cadastroDto.ativo());
            cadastro.setTipoCadastro(cadastroDto.TipoCadastro());

            Cadastro cadastroSalvo = cadastroService.save(cadastro);

            return new ResponseEntity<>(CadastroDto.from(cadastroSalvo), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Atualizar um cadastro no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao atualizar o cadastro"),
        @ApiResponse(responseCode = "400", description = "Falha ao atualizar o cadastro no banco de dados, verifique a requisição")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
        @Valid @RequestBody CadastroDto cadastroDto, @PathVariable Long id
    ) {
        log.info("update( " + cadastroDto + ", Id " + id + " )");

        Optional<Cadastro> cadastroData = cadastroService.findById(id);

        if (cadastroData.isPresent()) {
            Cadastro cadastro = cadastroData.get();
            cadastro.setId(id);
            cadastro.setNomeCompleto(cadastroDto.nomeCompleto());
            cadastro.setEmail(cadastroDto.email());
            cadastro.setCpf(cadastroDto.CPF());
            cadastro.setTelefone(cadastroDto.telefone());
            cadastro.setAtivo(cadastroDto.ativo());
            cadastro.setTipoCadastro(cadastroDto.TipoCadastro());

            cadastroService.save(cadastro);

            return new ResponseEntity<>("Cadastro alterado com sucesso!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Não foi possível encontrar o cadastro.", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary="Remover um cadastro do banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao remover o cadastro"),
        @ApiResponse(responseCode = "400", description = "Falha ao remover o cadastro, verifique a requisição")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.info("delete( Id " + id + " )");

        try {
            cadastroService.deleteById(id);
            return new ResponseEntity<>("Cadastro excluído com sucesso!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Não foi possível excluir o cadastro.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
