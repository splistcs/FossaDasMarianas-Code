package fdmapi.realizarpedido.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.StatusPedido;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {
  Pedido findByIdSessao(Integer idSessao);

  /* Obs. Depende da implementação de Cliente. */
   @Query("SELECT p "
         +"FROM Cadastro c JOIN c.pedidos p "
         +"WHERE c.id = :cadastroId AND p.statusPedido = :statusPedido")

  Pedido selectPorCadastroIdEStatusPedido(
      @Param("cadastroId") Long cadastroId, 
      @Param("statusPedido") StatusPedido statusPedido);
}
