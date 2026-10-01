package fdmapi.cadastro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fdmapi.cadastro.model.Cadastro;

public interface CadastroRepository extends CrudRepository<Cadastro, Long> {
    public List<Cadastro> findByCadastro(Cadastro cadastro);    
}
