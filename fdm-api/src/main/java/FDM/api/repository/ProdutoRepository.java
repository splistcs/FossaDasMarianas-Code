package FDM.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import FDM.api.model.Categoria;
import FDM.api.model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long>{
    public List<Produto> findByCategoria(Categoria categoria);
}
