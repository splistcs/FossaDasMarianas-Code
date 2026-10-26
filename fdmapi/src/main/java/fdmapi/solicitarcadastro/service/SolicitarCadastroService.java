package fdmapi.solicitarcadastro.service;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fdmapi.cadastro.dto.CadastroRespostaDto;
import fdmapi.cadastro.model.Cadastro;
import fdmapi.cadastro.service.CadastroService;
import fdmapi.comum.erro.ConflitoException;
import fdmapi.endereco.dto.EnderecoDto;
import fdmapi.endereco.model.Endereco;
import fdmapi.solicitarcadastro.dto.SolicitacaoCadastroDto;

/**
 * UC01 - Solicitar Cadastro.
 *
 * Seguindo o padrão do UC07, este service não acessa o repositório
 * diretamente: usa o CadastroService, que é o dono do domínio "cadastro".
 */
@Service
public class SolicitarCadastroService {

    private final CadastroService cadastroService;

    public SolicitarCadastroService(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @Transactional
    public CadastroRespostaDto solicitar(SolicitacaoCadastroDto dto) {
        // Normalização: "529.982.247-25" e "52998224725" são o mesmo CPF,
        // e " Ana@Email.com " e "ana@email.com" são o mesmo e-mail.
        // Sem isso, a verificação de unicidade seria facilmente contornada.
        String email = dto.email().trim().toLowerCase(Locale.ROOT);
        String cpf = somenteDigitos(dto.cpf());

        // Regra: e-mail e CPF únicos. A restrição UNIQUE do banco continua
        // existindo como última barreira (ex.: duas requisições simultâneas).
        if (cadastroService.existeEmail(email)) {
            throw new ConflitoException("Já existe um cadastro com este e-mail.");
        }
        if (cadastroService.existeCpf(cpf)) {
            throw new ConflitoException("Já existe um cadastro com este CPF.");
        }

        // Regra: toda solicitação nasce como CLIENTE e inativa (pendente de aprovação, UC02)
        Cadastro cadastro = Cadastro.builder()
            .nomeCompleto(dto.nomeCompleto().trim())
            .email(email)
            .cpf(cpf)
            .telefone(somenteDigitos(dto.telefone()))
            .ativo(false)
            .tipoCadastro(Cadastro.TipoCadastro.CLIENTE)
            .build();

        dto.enderecos().forEach(enderecoDto -> cadastro.getEnderecos().add(paraEndereco(enderecoDto)));

        Cadastro salvo = cadastroService.cadastrar(cadastro, dto.senha());
        return CadastroRespostaDto.from(salvo);
    }

    // O id eventualmente enviado no DTO é ignorado: endereços de uma
    // solicitação são sempre novos.
    private Endereco paraEndereco(EnderecoDto dto) {
        return Endereco.builder()
            .cep(somenteDigitos(dto.cep()))
            .numero(dto.numero().trim())
            .complemento(dto.complemento() == null ? null : dto.complemento().trim())
            .bairro(dto.bairro().trim())
            .rua(dto.rua().trim())
            .cidade(dto.cidade().trim())
            .build();
    }

    private static String somenteDigitos(String valor) {
        return valor.replaceAll("\\D", "");
    }
}
