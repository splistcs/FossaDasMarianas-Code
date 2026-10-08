package fdmapi.realizarpedido.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Endereco;

public interface EnderecoRepository extends CrudRepository<Endereco, Long> {

}
