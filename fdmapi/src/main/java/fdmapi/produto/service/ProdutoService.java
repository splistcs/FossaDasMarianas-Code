package fdmapi.produto.service;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import fdmapi.produto.model.Produto;
import fdmapi.produto.dto.ProdutoDto;
import fdmapi.produto.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	private final ProdutoRepository produtoRepository;

	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

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

  /** 
   *  O método realiza a filtragem, pelo repository de Produto,
   *  de Produto distantes dos valores dado pelo ProdutoDto.
   *
   *  @param tipo define o grau de distância.
   *  @param dto  a ser comparado.
   *  @param categoriaId id da categoria para se considerar,
   *                     adotei pois não conseguir utilizar
   *                     com segurança a Categoria.
   *
   *  @see #SelectTipo()
   *  @see ProdutoRepository
   */
  public List<ProdutoDto> getDtoPor(SelectTipo tipo, ProdutoDto dto, Long categoriaId) {
    List<ProdutoDto> produtoDtos = new ArrayList<>();
    List<Produto> produtos = null;
    Long id = (categoriaId != null) ? categoriaId : null;

    switch (tipo) {
      case EXATO:
        produtos = produtoRepository.selectPorDtoExato(dto.nome(), dto.material(), 
                                                       dto.marca(), id);
        break;
      case PROXIMO:
        produtos = produtoRepository.selectPorDtoProximo(dto.nome(), dto.material(), 
                                                         dto.marca(), id);
        break;
      case SUGESTAO:
        produtos = produtoRepository.selectPorDtoSugestao(dto.material(),
                                                          dto.marca(), id);
        break;
    }

    for(Produto p : produtos) {
      produtoDtos.add(ProdutoDto.from(p));
    }

    return produtoDtos;
	}  

  public enum SelectTipo { 
    EXATO, 
    PROXIMO, 
    SUGESTAO 
  }
}
