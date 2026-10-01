package fdmapi.cadastro.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fdmapi.cadastro.model.Cadastro;
import fdmapi.cadastro.repository.CadastroRepository;

@Service
public class CadastroService {
    
    private final CadastroRepository cadastroRepository;

    public CadastroService(CadastroRepository cadastroRepository) {
        this.cadastroRepository = cadastroRepository;
    }

    public Iterable<Cadastro> getAll() {
        return cadastroRepository.findAll();
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
