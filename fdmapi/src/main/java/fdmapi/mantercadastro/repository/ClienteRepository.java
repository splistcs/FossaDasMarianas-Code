package fdmapi.realizarpedido.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Cliente;
import fdmapi.mantercadastro.model.TipoCadastro;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

}
