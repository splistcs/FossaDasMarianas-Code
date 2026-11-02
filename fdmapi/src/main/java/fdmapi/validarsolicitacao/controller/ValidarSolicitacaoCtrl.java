package fdmapi.validarsolicitacao.controller;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.log4j.Log4j2;

import fdmapi.cadastro.dto.CadastroRespostaDto;
import fdmapi.validarsolicitacao.service.ValidarSolicitacaoService;

/*
 * Endpoints do gestor. Estão ABERTOS temporariamente: serão protegidos
 * no UC04 (Fazer Login), com uma regra única para o prefixo /fdm/gestor/**.
 */
@Tag(name = "Validar Solicitação de Cadastro", description = "UC02 - Aprovação ou recusa de solicitações de cadastro")
@Log4j2
@RequestMapping(path = "/fdm/gestor/solicitacoes")
@RestController
public class ValidarSolicitacaoCtrl {

    private final ValidarSolicitacaoService validarSolicitacaoService;

    public ValidarSolicitacaoCtrl(ValidarSolicitacaoService validarSolicitacaoService) {
        this.validarSolicitacaoService = validarSolicitacaoService;
    }

    @Operation(summary = "Listar as solicitações de cadastro pendentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitações pendentes (pode ser vazia)")
        }
    )
    @GetMapping
    public List<CadastroRespostaDto> listarPendentes() {
        log.info("listarPendentes()");
        return validarSolicitacaoService.listarPendentes();
    }

    @Operation(summary = "Aprovar uma solicitação de cadastro",
               description = "Ativa o cadastro, que passa a poder ser usado pelo cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitação aprovada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "O cadastro não está pendente de aprovação",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        }
    )
    @PostMapping("/{id}/aprovar")
    public CadastroRespostaDto aprovar(@PathVariable Long id) {
        log.info("aprovar( Id " + id + " )");
        return validarSolicitacaoService.aprovar(id);
    }

    @Operation(summary = "Recusar uma solicitação de cadastro",
               description = "Exclui a solicitação e seus endereços.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Solicitação recusada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "O cadastro não está pendente de aprovação",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        }
    )
    @PostMapping("/{id}/recusar")
    public ResponseEntity<Void> recusar(@PathVariable Long id) {
        log.info("recusar( Id " + id + " )");
        validarSolicitacaoService.recusar(id);
        // 204 No Content: operação concluída, sem corpo a devolver
        return ResponseEntity.noContent().build();
    }
}
