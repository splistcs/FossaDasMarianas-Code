package fdmapi.Produto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fdmapi.Produto.model.Categoria;
import fdmapi.Produto.model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long>{
    public List<Produto> findByCategoria(Categoria categoria);
}
