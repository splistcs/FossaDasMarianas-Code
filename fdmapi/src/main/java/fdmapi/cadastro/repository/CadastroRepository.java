package fdmapi.cadastro.repository;

import org.springframework.data.repository.CrudRepository;

import fdmapi.cadastro.model.Cadastro;

public interface CadastroRepository extends CrudRepository<Cadastro, Long> {
}
