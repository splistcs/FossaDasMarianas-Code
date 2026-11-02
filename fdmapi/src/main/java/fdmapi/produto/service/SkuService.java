package fdmapi.produto.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fdmapi.produto.model.Sku;
import fdmapi.produto.dto.SkuDto;
import fdmapi.produto.repository.SkuRepository;

@Service
public class SkuService {
	
	private final SkuRepository skuRepository;

	public SkuService(SkuRepository skuRepository) {
		this.skuRepository = skuRepository;
	}

  public Iterable<Sku> getAll() {
		return skuRepository.findAll();
	}

	public Sku save(Sku sku) {
		return skuRepository.save(sku);
	}

	public Optional<Sku> findById(Long id) {
		return skuRepository.findById(id);
	}

  public SkuDto findByIdDto(Long id) {
    Optional<Sku> s = skuRepository.findById(id);
		SkuDto skuDto = (s.isEmpty()) ? null : SkuDto.from(s.get());
    return skuDto;
	}

	public void deleteById(Long id) throws Exception {
		try {
			if (skuRepository.findById(id).isEmpty()) {
				throw new Exception("Sku inexistente.");
			}
			skuRepository.deleteById(id);		
		}
		catch (Exception e) {
			throw e;
		}
	}  
}
