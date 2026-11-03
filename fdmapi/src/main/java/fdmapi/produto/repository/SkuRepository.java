package fdmapi.produto.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import fdmapi.produto.model.Sku;

public interface SkuRepository extends CrudRepository<Sku, Long>{

}
