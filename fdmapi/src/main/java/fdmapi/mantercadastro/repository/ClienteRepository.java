package fdmapi.mantercadastro.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Cliente;
import fdmapi.mantercadastro.model.TipoCadastro;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

  @Query("SELECT c.ativo "
        +"FROM Cliente c "
        +"WHERE c.id = :clienteId")
  Boolean selectClienteGetAtivo(@Param("clienteId") Long clienteId);
}
