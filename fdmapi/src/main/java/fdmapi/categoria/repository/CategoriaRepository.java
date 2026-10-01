package fdmapi.categoria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fdmapi.categoria.model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Long>{
    public List<Categoria> findByCategoria(Categoria categoria);
}
