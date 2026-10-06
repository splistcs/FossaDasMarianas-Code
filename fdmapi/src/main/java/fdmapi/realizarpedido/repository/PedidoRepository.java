package fdmapi.realizarpedido.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.produto.model.Produto;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {

  /* Obs. Depende da implementação de Cliente. */
  // @Query("SELECT p "
  //       +"FROM Cliente c JOIN c.pedido p "
  //       +"WHERE c.id = :clienteId")
  //
  // List<Item> selectPorClienteDto(@Param("clienteId") Long clienteId);
}
