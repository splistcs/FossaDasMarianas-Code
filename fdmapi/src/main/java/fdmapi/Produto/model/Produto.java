package fdmapi.produto.model;

import java.util.ArrayList;
import java.util.List;

import fdmapi.categoria.model.Categoria;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private Boolean ativo;

    @Column
    private String imagemPrincipalUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "produto_id")
    private List<Sku> skus = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public void setSkus(List<Sku> skus) {
        this.skus.clear();

        if (skus != null) {
            this.skus.addAll(skus);
        }
    }
}
