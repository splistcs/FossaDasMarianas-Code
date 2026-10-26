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

    /*
     * Os métodos *Dto convertem as entidades dentro da transação, enquanto
     * a lista de endereços (LAZY) ainda pode ser carregada do banco.
     */
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
     * Use este método para cadastros novos; o save() não altera a senha.
     */
    @Transactional
    public Cadastro cadastrar(Cadastro cadastro, String senha) {
        cadastro.setSenha(passwordEncoder.encode(senha));
        return cadastroRepository.save(cadastro);
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
