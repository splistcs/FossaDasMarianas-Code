package fdmapi.produto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdmapi.produto.model.Produto;
import fdmapi.produto.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;

	public Iterable<Produto> getAll() {		
		return produtoRepository.findAll();
	}

	public Produto save(Produto produto) {
		return produtoRepository.save(produto);
	}

	public Optional<Produto> findById(Long id) {
		return produtoRepository.findById(id);
	}

	public void deleteById(Long id) throws Exception {
		try {
			if (produtoRepository.findById(id).isEmpty()) {
				throw new Exception("Produto inexistente.");
			}
			produtoRepository.deleteById(id);		
		}
		catch (Exception e) {
			throw e;
		}
	}
}
