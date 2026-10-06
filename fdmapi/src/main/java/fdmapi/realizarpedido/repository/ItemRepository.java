package fdmapi.realizarpedido.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {

  @Query("SELECT i "
        +"FROM Pedido p JOIN p.itemPedido i "
        +"WHERE p.id = :pedidoId")

  List<Item> selectPorPedidoDto(@Param("pedidoId") Long pedidoId);
}
