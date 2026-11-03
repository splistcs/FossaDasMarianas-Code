package fdmapi.endereco.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;

/* FossaDasMarinanas fdmapi import */
import fdmapi.endereco.model.Endereco;

public interface EnderecoRepository extends CrudRepository<Endereco, Long> {

}
