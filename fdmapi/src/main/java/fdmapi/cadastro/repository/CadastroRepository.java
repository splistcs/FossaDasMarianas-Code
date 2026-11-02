package fdmapi.cadastro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fdmapi.cadastro.model.Cadastro;

public interface CadastroRepository extends CrudRepository<Cadastro, Long> {

    // Consultas derivadas do nome do método (Spring Data):
    // geram SELECT ... WHERE email = ? e SELECT ... WHERE cpf = ?
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    // SELECT ... WHERE ativo = false AND tipo_cadastro = ?
    List<Cadastro> findByAtivoFalseAndTipoCadastro(Cadastro.TipoCadastro tipoCadastro);
}