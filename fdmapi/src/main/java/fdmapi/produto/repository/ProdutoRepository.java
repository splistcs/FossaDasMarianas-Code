package fdmapi.produto.repository;

/* Java import */
import java.util.List;

/* Spring import*/
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/* FossaDasMarinanas fdmapi import */
import fdmapi.categoria.model.Categoria;
import fdmapi.produto.model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long>{
  public List<Produto> findByCategoria(Categoria categoria);

  /** 
   *  Os Query abaixo são script SQL do spring-boot
   *  para realizar a filtragem no nível do db, para,
   *  minimizar, e mesmo eliminar, a manutenção
   *  de List de referência e suas cópias.
   *
   *  @param @Param()<T> Espera-se que chamadas.
   *                     dos selects forneçam 
   *                     pelos métodos def. em Dto.
   *
   *  @see ProdutoService#getDtoPor()
   *  @see PesquisarProdutoService
   *
   *  03/10 - DISCUTIR NA PROX. PRINT:
   *  [1]. Vantagens dessa solução em relação a método em java.
   */
  @Query("SELECT p FROM Produto p " 
        +"WHERE p.ativo = true AND (:nome IS NULL OR p.nome = :nome) "
        +                     "AND (:material IS NULL OR p.material = :material) "
        +                     "AND (:marca IS NULL OR p.marca = :marca) "
        +                     "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)")

  List<Produto> selectPorDtoExato(@Param("nome") String nome, @Param("material") String material,
                                  @Param("marca") String marca, @Param("categoriaId") Long categoriaId);

  @Query("SELECT p FROM Produto p " 
        +"WHERE p.ativo = true AND (:nome IS NULL OR p.nome LIKE CONCAT(:nome, '%')) "
        +                     "AND (:material IS NULL OR p.material LIKE CONCAT(:material, '%')) "
        +                     "AND (:marca IS NULL OR p.marca LIKE CONCAT(:marca, '%')) "
        +                     "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)")

  List<Produto> selectPorDtoProximo(@Param("nome") String nome, @Param("material") String material,
                                    @Param("marca") String marca, @Param("categoriaId") Long categoriaId);

  @Query("SELECT p FROM Produto p " 
        +"WHERE p.ativo = true AND (:material IS NULL OR p.material LIKE CONCAT(:material, '%')) "
        +                     "AND (:marca IS NULL OR p.marca LIKE CONCAT(:marca, '%')) "
        +                     "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)")

  List<Produto> selectPorDtoSugestao(@Param("material") String material, 
                                     @Param("marca") String marca, @Param("categoriaId") Long categoriaId);
}
