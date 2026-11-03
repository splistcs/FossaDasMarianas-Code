package fdmapi.cadastro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fdmapi.cadastro.model.Cadastro;
import fdmapi.cadastro.model.TipoCadastro;

public interface CadastroRepository extends CrudRepository<Cadastro, Long> {

    // Consultas derivadas do nome do método (Spring Data):
    // geram SELECT ... WHERE email = ? e SELECT ... WHERE cpf = ?
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    // SELECT ... WHERE ativo = false AND tipo_cadastro = ?
    List<Cadastro> findByAtivoFalseAndTipoCadastro(Cadastro.TipoCadastro tipoCadastro);  

    @Query("SELECT c.ativo "
          +"FROM Cadastro c "
          +"WHERE c.id = :cadastroId")
    Boolean selectCadastroGetAtivo(@Param("cadastroId") Long cadastroId);
}
