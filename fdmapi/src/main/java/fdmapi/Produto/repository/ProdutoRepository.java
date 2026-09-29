package fdmapi.produto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fdmapi.produto.model.Categoria;
import fdmapi.produto.model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long>{
    public List<Produto> findByCategoria(Categoria categoria);
}
