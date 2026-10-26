package fdmapi.solicitarcadastro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;

import fdmapi.cadastro.dto.CadastroRespostaDto;
import fdmapi.solicitarcadastro.dto.SolicitacaoCadastroDto;
import fdmapi.solicitarcadastro.service.SolicitarCadastroService;

@Tag(name = "Solicitar Cadastro", description = "UC01 - Solicitação de cadastro de cliente")
@Log4j2
@RequestMapping(path = "/fdm/solicitacoes-cadastro")
@RestController
public class SolicitarCadastroCtrl {

    private final SolicitarCadastroService solicitarCadastroService;

    public SolicitarCadastroCtrl(SolicitarCadastroService solicitarCadastroService) {
        this.solicitarCadastroService = solicitarCadastroService;
    }

    @Operation(summary = "Solicitar o cadastro de um novo cliente",
               description = "O cadastro é criado inativo e só poderá ser usado após a aprovação do gestor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitação registrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "E-mail ou CPF já cadastrado",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        }
    )
    @PostMapping
    public ResponseEntity<CadastroRespostaDto> solicitar(@Valid @RequestBody SolicitacaoCadastroDto dto) {
        // Só o e-mail vai para o log: dados pessoais (CPF, telefone, endereço)
        // não devem ser registrados sem necessidade.
        log.info("solicitar( email " + dto.email() + " )");

        CadastroRespostaDto resposta = solicitarCadastroService.solicitar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}
