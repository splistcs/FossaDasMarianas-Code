package fdmapi.validarsolicitacao.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fdmapi.cadastro.dto.CadastroRespostaDto;
import fdmapi.cadastro.model.Cadastro;
import fdmapi.cadastro.service.CadastroService;
import fdmapi.comum.erro.ConflitoException;
import fdmapi.comum.erro.NaoEncontradoException;
import lombok.extern.log4j.Log4j2;

/**
 * UC02 - Validar Solicitação de Cadastro.
 *
 * Orquestra o caso de uso usando o CadastroService, que concentra o que
 * significa uma solicitação estar "pendente".
 */
@Log4j2
@Service
public class ValidarSolicitacaoService {

    private final CadastroService cadastroService;

    public ValidarSolicitacaoService(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @Transactional(readOnly = true)
    public List<CadastroRespostaDto> listarPendentes() {
        return cadastroService.getSolicitacoesPendentes().stream()
            .map(CadastroRespostaDto::from)
            .toList();
    }

    @Transactional
    public CadastroRespostaDto aprovar(Long id) {
        Cadastro cadastro = buscarPendente(id);
        Cadastro aprovado = cadastroService.aprovarSolicitacao(cadastro);
        log.info("Solicitação de cadastro " + id + " aprovada.");
        return CadastroRespostaDto.from(aprovado);
    }

    /**
     * Nesta versão, recusar exclui o cadastro (e seus endereços). Assim,
     * o e-mail e o CPF ficam livres para uma nova solicitação.
     * Não há notificação ao solicitante; a recusa é apenas registrada no log.
     */
    @Transactional
    public void recusar(Long id) {
        Cadastro cadastro = buscarPendente(id);
        cadastroService.excluir(cadastro);
        log.info("Solicitação de cadastro " + id + " recusada e excluída.");
    }

    // Regras comuns a aprovar e recusar: a solicitação deve existir (404)
    // e estar pendente (409). Impede, por exemplo, "recusar" o gestor ou
    // aprovar duas vezes a mesma solicitação.
    private Cadastro buscarPendente(Long id) {
        Cadastro cadastro = cadastroService.findById(id)
            .orElseThrow(() -> new NaoEncontradoException("Solicitação de cadastro não encontrada."));

        if (!cadastroService.isSolicitacaoPendente(cadastro)) {
            throw new ConflitoException("Este cadastro não está pendente de aprovação.");
        }
        return cadastro;
    }
}
