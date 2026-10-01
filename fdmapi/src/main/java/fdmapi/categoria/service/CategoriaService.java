package fdmapi.categoria.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fdmapi.categoria.model.Categoria;
import fdmapi.categoria.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	private final CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	public Iterable<Categoria> getAll() {		
		return categoriaRepository.findAll();
	}

	public Categoria save(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public Optional<Categoria> findById(Long id) {
		return categoriaRepository.findById(id);
	}

	public void deleteById(Long id) throws Exception {
		try {
			if (categoriaRepository.findById(id).isEmpty()) {
				throw new Exception("Categoria inexistente.");
			}
			categoriaRepository.deleteById(id);		
		}
		catch (Exception e) {
			throw e;
		}
	}
}
