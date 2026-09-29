package fdmapi.Produto.model;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
public class Sku {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private BigDecimal preco;

    @ElementCollection
    @CollectionTable(name = "especificacoes")
    @MapKeyColumn(name ="nome")
    @Column(name = "valor")
    private Map<String, String> especificacoes;
    
    @Column(nullable = false)
    private Integer pesoGramas;

    @Column(nullable = false, unique = true) /* Confirmar se é necessário ser único */
    private String codigoUniversal;

    @Column(nullable = false)
    private Integer alturaCm;

    @Column(nullable = false)
    private Integer larguraCm;

    @Column(nullable = false)
    private Integer comprimentoCm;
}
