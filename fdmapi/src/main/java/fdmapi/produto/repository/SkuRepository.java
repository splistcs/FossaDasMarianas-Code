package fdmapi.produto.repository;

import org.springframework.data.repository.CrudRepository;

import fdmapi.produto.model.Sku;

public interface SkuRepository extends CrudRepository<Sku, Long>{
}
