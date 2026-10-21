package fdmapi.pesquisarproduto.service;

/* Java import */
import java.util.List;
import java.util.ArrayList;

/* Spring import*/
import org.springframework.stereotype.Service;

/* FossaDasMarinanas fdmapi import */
import fdmapi.produto.dto.ProdutoDto;
import fdmapi.produto.service.ProdutoService;

/* Misc. import */
import org.apache.commons.text.similarity.LevenshteinDistance;
import lombok.extern.log4j.Log4j2;

/**
 * PesquisarProdutoService
 * Objetivo é através do uso de outros Service atingir 
 * o propósito do Ctrl em diferentes graus de precisão.
 * @author Magocho
 */

@Service
public class PesquisarProdutoService {
	private final ProdutoService produtoService;

  public PesquisarProdutoService(ProdutoService produtoService) {
    this.produtoService = produtoService;
  }

  public Iterable<ProdutoDto> findByDto(ProdutoDto produtoDto, Long id) {
		return produtoService.getDtoPor(ProdutoService.SelectTipo.EXATO, 
                                    produtoDto, id);
	}

  public Iterable<ProdutoDto> findByDtoProximo(ProdutoDto produtoDto, Long id) {
		return produtoService.getDtoPor(ProdutoService.SelectTipo.PROXIMO,
                                    produtoDto, id);
	}

  /** 
   *  O método calc quantos saltos precisa para formar o nome, 
   *  se for muito ineficiente trocar por JaroWinklerDistance(),
   *  e retona todos os ProdutoDto que tenham a menor distância.
   *
   *  @see org.apache.commons.text.similarity.LevenshteinDistance#apply()
   *  @see ProdutoService#getDtoPor()
   *
   *  Fontes:
   *  Obs. a wiki tem um ótimo gif explicando:
   *  [url]: https://en.wikipedia.org/wiki/Levenshtein_distance
   *  [url]: https://stackoverflow.com/questions/327513/fuzzy-string-search-library-in-java
   *  [url]: https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/similarity/package-summary.html
   */
  public Iterable<ProdutoDto> findByDtoSugestao(ProdutoDto produtoDto, Long id) {
    int min = Integer.MAX_VALUE, aux = 0;
    final String nome = produtoDto.nome();
    List<ProdutoDto> sugestoes = produtoService.getDtoPor(ProdutoService.SelectTipo.SUGESTAO,
                                                          produtoDto, id);
    ProdutoDto melhorProduto = null;

    LevenshteinDistance levenshtein = new LevenshteinDistance();

    if(sugestoes.isEmpty() || produtoDto.nome() == null) {
		  return new ArrayList<>();
    }

    for (ProdutoDto p : sugestoes) {
      aux = levenshtein.apply(nome, p.nome());
      if(min > aux) {
        min = aux;
      }
    }

    final int distanciaMin = min;
    sugestoes.removeIf(p -> levenshtein.apply(nome, p.nome()) != distanciaMin);

		return sugestoes;
	}
}
