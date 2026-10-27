package fdmapi.cadastro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fdmapi.cadastro.dto.CadastroRespostaDto;
import fdmapi.cadastro.model.Cadastro;
import fdmapi.cadastro.repository.CadastroRepository;

@Service
public class CadastroService {

    private final CadastroRepository cadastroRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroService(CadastroRepository cadastroRepository, PasswordEncoder passwordEncoder) {
        this.cadastroRepository = cadastroRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<Cadastro> getAll() {
        return cadastroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CadastroRespostaDto> getAllDto() {
        List<CadastroRespostaDto> dtos = new ArrayList<>();
        cadastroRepository.findAll().forEach(cadastro -> dtos.add(CadastroRespostaDto.from(cadastro)));
        return dtos;
    }

    @Transactional(readOnly = true)
    public Optional<CadastroRespostaDto> getDtoPorId(Long id) {
        return cadastroRepository.findById(id).map(CadastroRespostaDto::from);
    }

    /**
     * Grava um cadastro novo, armazenando apenas o hash BCrypt da senha.
     * Use este método para cadastros novos.
     */
    @Transactional
    public Cadastro cadastrar(Cadastro cadastro, String senha) {
        cadastro.setSenha(passwordEncoder.encode(senha));
        return cadastroRepository.save(cadastro);
    }

    public boolean existeEmail(String email) {
        return cadastroRepository.existsByEmail(email);
    }

    public boolean existeCpf(String cpf) {
        return cadastroRepository.existsByCpf(cpf);
    }

    /*
     * Solicitações de cadastro (UC02).
     * Nesta versão, "solicitação pendente" = cliente inativo. Esse significado
     * fica concentrado nos três métodos abaixo: se o modelo ganhar um status
     * próprio (ex.: enum StatusCadastro), só eles precisam mudar.
     */
    public List<Cadastro> getSolicitacoesPendentes() {
        return cadastroRepository.findByAtivoFalseAndTipoCadastro(Cadastro.TipoCadastro.CLIENTE);
    }

    public boolean isSolicitacaoPendente(Cadastro cadastro) {
        return !cadastro.isAtivo() && cadastro.getTipoCadastro() == Cadastro.TipoCadastro.CLIENTE;
    }

    public Cadastro aprovarSolicitacao(Cadastro cadastro) {
        cadastro.setAtivo(true);
        return cadastroRepository.save(cadastro);
    }

    // Exclui o cadastro e, pela cascata, os seus endereços
    public void excluir(Cadastro cadastro) {
        cadastroRepository.delete(cadastro);
    }

    public Cadastro save(Cadastro cadastro) {
        return cadastroRepository.save(cadastro);
    }

    public Optional<Cadastro> findById(Long id) {
        return cadastroRepository.findById(id);
    }

    public void deleteById(Long id) throws Exception {
        try {
            if (cadastroRepository.findById(id).isEmpty()) {
                throw new Exception("Cadastro inexistente.");
            }
            cadastroRepository.deleteById(id);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
